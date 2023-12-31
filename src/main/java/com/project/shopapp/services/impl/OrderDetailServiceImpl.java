package com.project.shopapp.services.impl;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.responses.OrderDetailResponse;
import com.project.shopapp.services.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements IOrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    @Override
    public OrderDetailResponse createOrderDetails(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()->
                        new DataNotFoundException("Can not find Order with id: "+orderDetailDTO.getOrderId()));
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(()->
                        new DataNotFoundException("Can not find Product with id: "+orderDetailDTO.getProductId()));
        OrderDetail orderDetail = OrderDetailDTO.toEntity(orderDetailDTO);
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetailRepository.save(orderDetail);
        return OrderDetailResponse.toResponse(orderDetail);
    }

    @Override
    public OrderDetailResponse updateOrderDetails(long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(()->
                        new DataNotFoundException("Can not find OrderDetail with id: "+id));
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()->
                        new DataNotFoundException("Can not find Order with id: "+orderDetailDTO.getOrderId()));
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(()->
                        new DataNotFoundException("Can not find Product with id: "+orderDetailDTO.getProductId()));
        orderDetail.setProduct(product);
        orderDetail.setOrder(order);
        orderDetail.setColor(orderDetailDTO.getColor());
        orderDetail.setPrice(orderDetailDTO.getPrice());
        orderDetail.setNumberOfProduct(orderDetailDTO.getQuantity());
        orderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        orderDetailRepository.save(orderDetail);
        return OrderDetailResponse.toResponse(orderDetail);
    }

    @Override
    public OrderDetailResponse getOrderDetails(long id) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException("Can not find OrderDetail with id: "+id));
        return OrderDetailResponse.toResponse(orderDetail);
    }

    @Override
    public void deleteOrderDetail(long id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(null);
        orderDetailRepository.delete(orderDetail);
    }

    @Override
    public List<OrderDetailResponse> findByOrderId(long orderId) {
        List<OrderDetailResponse> orderDetailResponseList = new ArrayList<>();
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (orderDetailList.isEmpty()){
            return null;
        }
        orderDetailList.forEach(orderDetail -> {
            orderDetailResponseList.add(OrderDetailResponse.toResponse(orderDetail));
        });
        return orderDetailResponseList;
    }
}
