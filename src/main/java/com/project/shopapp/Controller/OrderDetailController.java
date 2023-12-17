package com.project.shopapp.Controller;

import com.project.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
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
            return ResponseEntity.ok("created order detail successfully!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable Long id){
        return ResponseEntity.ok("get order detail!"+ id);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getOrderDetailByOrder(@Valid @PathVariable Long orderId){
//        List<OrderDetail> listOrderDetail = orderDetailService.getOrderDetails(orderId);
        return ResponseEntity.ok("listOrderDetail!"+ orderId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable Long id,
                                               @Valid @RequestBody OrderDetailDTO orderDetailDTO){
        return ResponseEntity.ok("update order detail! "+id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> DeleteOrderDetail(@Valid @PathVariable Long id){
        return ResponseEntity.noContent().build();
    }
}
