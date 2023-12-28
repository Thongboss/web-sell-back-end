package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException;

    OrderResponse getOrder(long id) throws DataNotFoundException;

    OrderDTO updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(long id) throws DataNotFoundException;

    List<OrderResponse> findByUserId(long userId) throws DataNotFoundException;
}
