package com.project.shopapp.services.impl;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderStatus;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.responses.OrderResponse;
import com.project.shopapp.services.IOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        // tìm xem order_id có tồn ti không
        User user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Can not find user with id: "+orderDTO.getUserId()));
        // convert UserDTO to User
        // dùng thư viện ModelMapper
        //Tạo một luồng bảng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        //Cập nhật các trường của đơn hàng từ order DTO
        Order order = new Order();
        modelMapper.map(orderDTO,order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        //kiểm tra ngày ship hàng >= ngày tạo đơn hàng
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at least day!");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        return modelMapper.map(order,OrderResponse.class);
    }

    @Override
    public OrderResponse getOrder(long id) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find order with id: "+id));
        return modelMapper.map(order,OrderResponse.class);
    }

    @Override
    public OrderDTO updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find Order with id: "+id));
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find User with id: "+id));
        Order newOrder = OrderDTO.convertDTOToOrder(orderDTO);
        newOrder.setId(order.getId());
        newOrder.setUser(user);
        newOrder.setActive(true);
        orderRepository.save(newOrder);
        modelMapper.map(newOrder, orderDTO);
        return orderDTO;
    }

    @Override
    public void deleteOrder(long id) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find Order with id: "+id));
        order.setActive(false);
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponse> findByUserId(long userId) throws DataNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Can not User with id: "+userId));
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderResponse> listOrderRes = new ArrayList<>();
        orders.forEach(order -> {
            OrderResponse orderResponse = new OrderResponse();
            modelMapper.map(order, orderResponse);
            listOrderRes.add(orderResponse);
        });
        return listOrderRes;
    }
}
