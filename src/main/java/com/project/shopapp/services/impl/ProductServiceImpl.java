package com.project.shopapp.services.impl;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() ->
                        new DataNotFoundException("Cannot find category with categoryId: "
                                +productDTO.getCategoryId()));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .category(existingCategory)
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id: "+ id));
    }

    @Override
    public Page<Product> getAllProducts(PageRequest pageRequest) {
        //Lấy danh sách sản phẩm theo trang(page) và giới hạn(limit)
        return productRepository.findAll(pageRequest);
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id: "+ id));
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() ->
                        new DataNotFoundException("Cannot find category with categoryId: "
                                +productDTO.getCategoryId()));
        existingProduct.setCategory(existingCategory);
        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setThumbnail(productDTO.getThumbnail());
        existingProduct.setDescription(productDTO.getDescription());
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> optProduct = productRepository.findById(id);
        optProduct.ifPresent(product -> productRepository.delete(product));
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long id, ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id: "+ id));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        // không cho insert 5 ảnh trên 1 sản phẩm
        int size = productImageRepository.findByProductId(id).size();
        if (size >= 5){
            throw new InvalidParamException("Number of image must be <= 5");
        }
        return productImageRepository.save(newProductImage);
    }
}
