package com.example.demo.repository;

import com.example.demo.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FoodRepository extends JpaRepository<Food, Long > {

    Food findFoodById(Long id);

    List<Food> findAllByIsDeleteFalse();


}
