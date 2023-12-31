package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.OrderDetail;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "Order's Id must be > 0")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's Id must be > 0")
    private Long productId;

    @Min(value = 1, message = "Price must be > 0")
    private Float price;

    @Min(value = 1, message = "Product's quantity must be > 0")
    private int quantity;

    @JsonProperty("total_money")
    @Min(value = 1, message = "Total money must be > 0")
    private Float totalMoney;

    private String color;

    public static OrderDetail toEntity(OrderDetailDTO orderDetailDTO){
        return OrderDetail.builder()
                .color(orderDetailDTO.getColor())
                .numberOfProduct(orderDetailDTO.getQuantity())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .build();
    }
}
