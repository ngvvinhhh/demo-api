package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartItemResponse {
    private Long cartId;
    private String name;
    private double price;
    private String image;
    private int quantity;
    private double totalAmount;
}
