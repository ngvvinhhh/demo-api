package com.example.demo.exception;

public class FoodNotFound extends RuntimeException {
    public FoodNotFound(String message) {
        super(message);
    }
}
