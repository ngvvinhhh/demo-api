package com.example.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class OrdersResponse {
    private Long id;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private String customerPhone;
    private String productImage;
    private String productName;
    private int quantity;
    private double totalAmount;

}
