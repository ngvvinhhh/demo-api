package com.example.demo.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProfileRequest {
    @Email(message = "Email not valid!")
    String email;
    String name;
    @Pattern(regexp = "^(0[2-9]|84[2-9])[0-9]{8}$", message = "Phone number must follow Vietnam format (e.g., 090, 091, 84xx)")
    String phone;
    String address;
    @Pattern(regexp = "^(male|female|other)$", message = "Gender must be 'male', 'female', or 'other'")
    String gender;
}
