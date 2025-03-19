package com.example.demo.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FoodResponse {
    long id;
    String image;
    String name;
    String description;
    float price;
    String category;
    String petType;
    String foodType;
    LocalDateTime create_at;
}
