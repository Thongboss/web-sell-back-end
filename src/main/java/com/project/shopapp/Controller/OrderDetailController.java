package com.project.shopapp.Controller;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.responses.OrderDetailResponse;
import com.project.shopapp.services.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {

    private final IOrderDetailService orderDetailService;
    @PostMapping
    public ResponseEntity<?> addOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO, BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> erorrMesage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(erorrMesage);
            }
            OrderDetailResponse orderDetailResponse = orderDetailService.createOrderDetails(orderDetailDTO);
            return ResponseEntity.ok(orderDetailResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable Long id){
        try {
            OrderDetailResponse orderDetailResponse = orderDetailService.getOrderDetails(id);
            return ResponseEntity.ok(orderDetailResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getOrderDetailByOrder(@Valid @PathVariable Long orderId){
        try {
            List<OrderDetailResponse> orderDetailResponseList = orderDetailService.findByOrderId(orderId);
            return ResponseEntity.ok(orderDetailResponseList);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable Long id,
                                               @Valid @RequestBody OrderDetailDTO orderDetailDTO){
        try {
            OrderDetailResponse orderDetailResponse = orderDetailService.updateOrderDetails(id, orderDetailDTO);
            return ResponseEntity.ok(orderDetailResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> DeleteOrderDetail(@Valid @PathVariable Long id){
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.noContent().build();
    }
}
