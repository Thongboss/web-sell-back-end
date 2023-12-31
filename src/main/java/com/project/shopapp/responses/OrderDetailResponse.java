package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    private Long id;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("product_id")
    private Long productId;

    private Float price;

    @JsonProperty("number_of_product")
    private int numberOfProduct;

    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;

    public static OrderDetailResponse toResponse(OrderDetail orderDetail){
        return OrderDetailResponse.builder()
                .orderId(orderDetail.getOrder().getId())
                .productId(orderDetail.getProduct().getId())
                .color(orderDetail.getColor())
                .totalMoney(orderDetail.getTotalMoney())
                .numberOfProduct(orderDetail.getNumberOfProduct())
                .price(orderDetail.getPrice())
                .id(orderDetail.getId())
                .build();
    }
}
