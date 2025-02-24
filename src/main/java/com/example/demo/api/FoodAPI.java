package com.example.demo.api;

import com.example.demo.entity.Food;
import com.example.demo.model.FoodRequest;
import com.example.demo.model.FoodResponse;
import com.example.demo.model.UpdateFoodResponse;
import com.example.demo.service.FoodService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/food")
@SecurityRequirement(name = "api")
public class FoodAPI {

    @Autowired
    FoodService foodService;

    @GetMapping("/all")
    public ResponseEntity<List<FoodResponse>> getAll() {
        List<FoodResponse> foods = foodService.getAllFoods();
        return ResponseEntity.ok(foods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodResponse> getFoodById(@PathVariable long id) {
        FoodResponse foodResponse = foodService.getFoodById(id);
        return ResponseEntity.ok(foodResponse);
    }


    @PostMapping
    public  ResponseEntity<FoodResponse> create(@Valid @RequestBody FoodRequest food) {
        FoodResponse newFood = foodService.createFood(food);
        return ResponseEntity.ok(newFood);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateFoodResponse> updateFood(
            @PathVariable long id,
            @RequestBody FoodRequest foodRequest) {

        UpdateFoodResponse updatedFood = foodService.updateFood(id, foodRequest);
        return ResponseEntity.ok(updatedFood);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFood(@PathVariable long id) {
        foodService.deleteFood(id);
        return ResponseEntity.ok("Food deleted successfully!");
    }
}
