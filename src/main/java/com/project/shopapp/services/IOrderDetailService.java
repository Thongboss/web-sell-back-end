package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.responses.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    OrderDetailResponse createOrderDetails(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    OrderDetailResponse updateOrderDetails(long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    OrderDetailResponse getOrderDetails(long id) throws DataNotFoundException;

    void deleteOrderDetail(long id);

    List<OrderDetailResponse> findByOrderId(long orderId);
}
