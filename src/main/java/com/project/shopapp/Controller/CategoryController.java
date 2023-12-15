package com.project.shopapp.Controller;

import com.project.shopapp.dtos.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
//@Validated
public class CategoryController {
    @GetMapping
    public ResponseEntity<String> getAllCategories(@RequestParam("page") int page, @RequestParam("limit") int limit){
        return ResponseEntity.ok(String.format("Chào con gà! page = %d, limit = %d",page,limit));
    }

    @PostMapping
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result){
        if (result.hasErrors()){
            List<String> erorrMesage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(erorrMesage);
        }
        return ResponseEntity.ok("Thêm category done!"+ categoryDTO.getName());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable int id){
        return ResponseEntity.ok("Update Thành công! "+id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id){
        return ResponseEntity.ok("Delete successfully! " + id);
    }
}
