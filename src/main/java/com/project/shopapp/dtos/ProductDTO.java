package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Product name is required")
    @Size(min = 5 ,max = 200, message = "Product name must be between 5 and 200 characters")
    private String name;
    @Min(value = 0, message = "Product's price must be greater than or equal to 0")
    @Max(value = 1000000, message = "Product's price must be less than or equal 1.000.000")
    private Float price;
    private String thumbnail;
    @Min(value = 0, message = "Product's price must be greater than or equal to 0")
    @Max(value = 10000, message = "Product's quantity must be less than or equal 10.000")
    private int quantity;
    private String description;
    @JsonProperty("category_id")
    private String categoryId;
    private List<MultipartFile> files;
}
