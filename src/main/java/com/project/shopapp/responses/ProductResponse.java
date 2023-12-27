package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse extends BaseResponse{
    private String name;
    private Float price;
    private String thumbnail;
    private int quantity;
    private String description;
    @JsonProperty("category_id")
    private Long categoryId;

    public static ProductResponse convertProductToResponse(Product product){
        ProductResponse productResponse = ProductResponse.builder()
                .categoryId(product.getCategory().getId())
                .description(product.getDescription())
                .price(product.getPrice())
                .name(product.getName())
                .quantity(product.getQuantity())
                .thumbnail(product.getThumbnail())
                .build();
        productResponse.setCreateAt(product.getCreateAt());
        productResponse.setUpdateAt(product.getUpdateAt());
        return productResponse;
    }
}
