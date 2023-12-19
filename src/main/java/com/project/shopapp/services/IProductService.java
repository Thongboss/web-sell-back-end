package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    Product getProductById(Long id) throws Exception;

    Page<Product> getAllProducts(PageRequest pageRequest);

    Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException;

    void deleteProduct(Long id);

    boolean existsByName(String name);

    public ProductImage createProductImage(Long id, ProductImageDTO productImageDTO) throws Exception;
}
