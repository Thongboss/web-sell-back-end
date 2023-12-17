package com.project.shopapp.Controller;

import com.project.shopapp.dtos.ProductDTO;
import jakarta.validation.Valid;
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
    @GetMapping
    public ResponseEntity<?> getAllProduct(@RequestParam("page") int page, @RequestParam("limit") int limit){
        return ResponseEntity.ok(String.format("List products for page = %d and limit = %d", page, limit));
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(@Valid @ModelAttribute ProductDTO productDTO,
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
            List<MultipartFile> files = productDTO.getFiles();
            files = files == null ? new ArrayList<MultipartFile>() : files;
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
            }
            return ResponseEntity.ok("Add product successfully! ");
        }catch (Exception e){
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

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id){
        return ResponseEntity.ok("Update Thành công! "+id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        return ResponseEntity.ok("Delete successfully! " + id);
    }
}
