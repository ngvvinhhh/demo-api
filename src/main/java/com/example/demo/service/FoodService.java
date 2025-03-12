package com.example.demo.service;

import com.example.demo.entity.Food;
import com.example.demo.exception.FoodNotFound;
import com.example.demo.model.*;
import com.example.demo.repository.FoodRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FoodService {

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    ModelMapper modelMapper;

    public Food create(Food food) {
        return foodRepository.save(food);
    }

    public FoodResponse createFood(FoodRequest foodRequest) {

        Food food = modelMapper.map(foodRequest, Food.class);
        food.setCreate_at(LocalDateTime.now());
        Food newFood = foodRepository.save(food);

        return modelMapper.map(newFood, FoodResponse.class);
    }


    public UpdateFoodResponse updateFood(long id, FoodRequest foodRequest) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodNotFound("Food not found with id: " + id));

        if (foodRequest.getImage() != null && !foodRequest.getImage().trim().isEmpty()) {
            food.setImage(foodRequest.getImage());
        }
        if (foodRequest.getName() != null && !foodRequest.getName().trim().isEmpty()) {
            food.setName(foodRequest.getName());
        }
        if (foodRequest.getDescription() != null && !foodRequest.getDescription().trim().isEmpty()) {
            food.setDescription(foodRequest.getDescription());
        }
        if (foodRequest.getPrice() > 0) {
            food.setPrice(foodRequest.getPrice());
        }

        food.setUpdate_at(LocalDateTime.now());

        Food updatedFood = foodRepository.save(food);
        return modelMapper.map(updatedFood, UpdateFoodResponse.class);
    }

    public void deleteFood(long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodNotFound("Food not found with id: " + id));

        food.setDelete(true);
        foodRepository.save(food);
    }

    public FoodResponse getFoodById(long id) {
        Food food = foodRepository.findById(id)
                .filter(f -> !f.isDelete())
                .orElseThrow(() -> new FoodNotFound("Food not found with id: " + id));

        return modelMapper.map(food, FoodResponse.class);
    }




    public List<FoodResponse> getAllFoods() {
        List<Food> foods = foodRepository.findAllByIsDeleteFalse();
        return foods.stream()
                .map(food -> modelMapper.map(food, FoodResponse.class))
                .toList();
    }


}
