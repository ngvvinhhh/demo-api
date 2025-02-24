package com.example.demo.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FoodRequest {
    @NotBlank(message = "Image food is not blank!")
    String image;
    @NotBlank(message = "Food name is not blank!")
    String name;
    @NotBlank(message = "Description Food is not blank!")
    String description;
    @Min(value = 0, message = "The price of the dish cannot be less than 0!")
    float price;
}
