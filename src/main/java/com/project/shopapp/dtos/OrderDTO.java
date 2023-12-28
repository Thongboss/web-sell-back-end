package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.shopapp.models.Order;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @JsonProperty("user_id")
    @Min(value = 1, message = "UserId must be greater than 0")
    private Long userId;

    @JsonProperty("fullname")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required!")
    @Size(min = 10, max = 10, message = "phone number must be 10 character!")
    private String phoneNumber;

    private String address;

    private String note;

    private String status;

    @JsonProperty("order_date")
    private Date orderDate;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be greater than or equal 0")
    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("tracking_number")
    private String trackingNumber	;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    public static Order convertDTOToOrder(OrderDTO orderDTO){
        return Order.builder()
                .shippingDate(orderDTO.getShippingDate())
                .email(orderDTO.getEmail())
                .address(orderDTO.getAddress())
                .note(orderDTO.getNote())
                .status(orderDTO.getStatus())
                .fullName(orderDTO.getFullName())
                .paymentMethod(orderDTO.getPaymentMethod())
                .phoneNumber(orderDTO.getPhoneNumber())
                .shippingAddress(orderDTO.getShippingAddress())
                .totalMoney(orderDTO.getTotalMoney())
                .trackingNumber(orderDTO.getTrackingNumber())
                .orderDate(orderDTO.getOrderDate())
                .shippingMethod(orderDTO.getShippingMethod())
                .build();
    }
}
