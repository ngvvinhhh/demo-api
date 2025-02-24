package com.example.demo.model;

import lombok.Data;

@Data
public class AccountResponse {
    long id;
    String email;
    String name;
    String token;
}
