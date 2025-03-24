package com.example.demo.model;

import lombok.Data;

@Data
public class ProfileResponse {
    long id;
    String email;
    String name;
    String phone;
    String address;
    String gender;
}
