package com.project.shopapp.Controller;

import com.github.javafaker.Faker;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.services.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping
    public ResponseEntity<ProductListResponse> getAllProduct(@RequestParam("page") int page,
                                                             @RequestParam("limit") int limit){
        //Tạo pageable thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createAt").descending());

        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        // Lấy ra tổng số trang
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(products)
                        .totalPages(totalPages)
                .build());
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDTO productDTO,
//                                        @RequestPart("file") MultipartFile file,
                                        BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> erorrMesage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(erorrMesage);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProductImage(
            @PathVariable("id") Long id,
            @ModelAttribute("files") List<MultipartFile> files){
        try {
            Product existingProduct = productService.getProductById(id);
            List<ProductImage> productImages = new ArrayList<>();

            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body("You can only upload maximum 5 images");
            }
            for (MultipartFile file : files){
                if (file.getSize() == 0){
                    continue;
                }
                //Kiểm tra kích thước file và định dạng
                if (file.getSize() > 10 * 1024 * 1024){// kích thước > 10M
//                throw new ResponseStatusException(
//                        HttpStatus.PAYLOAD_TOO_LARGE, "File too large! Maximum size is 10MB"
//                );
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File too large! Maximum size is 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                //Lưu file và cập nhật thumbnail trong ProductDTO
                String fileName = storeFile(file);
                ProductImage newProductImage = productService.createProductImage(
                        existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(fileName)
                                .build());
                productImages.add(newProductImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//    {
//        "name": "Yasuo ma kiếm",
//            "price": 100,
//            "thumbnail": "https://",
//            "quantity": 300,
//            "description": "null",
//            "category_id": "10"
//    }
    private String storeFile(MultipartFile file) throws IOException {

        if (!(isImageFile(file)) || file.getOriginalFilename() == null){
            throw new IOException("Invalid image format");
        }
        //Lấy ra tên gốc của file
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        //Thêm UUID vào trước tên file đảm bảo tên fiel là duy nhất không bị trùng.
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

        //Đường dẫn đến thư mục mà bạn muốn lưu file
        Path uploadDir = Paths.get("uploads");

        if (!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        //Tạo đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(),uniqueFileName);

        //Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneProduct(@PathVariable("id") long id){
            try {
                Product product = productService.getProductById(id);
                return ResponseEntity.ok(ProductResponse.convertProductToResponse(product));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") long id,
                                                @RequestBody ProductDTO productDTO){
        try {
            Product product = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(ProductResponse.convertProductToResponse(product));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Delete successfully! " + id);
    }

    private boolean isImageFile(MultipartFile file){
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

//    @PostMapping("/generateFakeProducts")
    public ResponseEntity<String> generateFakeProducts(){
        Faker faker = new Faker();
        for (int i= 0; i < 3000; i++){
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)){
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(50,2000))
                    .description(faker.lorem().sentence())
                    .categoryId((long)faker.number().numberBetween(2,7))
                    .quantity(faker.number().numberBetween(100,1000))
                    .build();
            try {
                productService.createProduct(productDTO);
            }catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("fake product created successfully!");
    }
}
