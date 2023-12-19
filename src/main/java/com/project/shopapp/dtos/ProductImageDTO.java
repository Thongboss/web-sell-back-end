package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDTO {
    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's Id must be > 0")
    private Long productId;

    @Size(min = 5, max = 250, message = "Image url must be between 5 and 250 character!")
    @JsonProperty("image_url")
    private String imageUrl;
}
