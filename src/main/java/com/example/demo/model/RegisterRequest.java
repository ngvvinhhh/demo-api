package com.example.demo.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    String name;
    @Email(message = "Email not valid!")
    String email;
    @NotBlank(message = "Password can not blank!")
    @Size(min = 6, message = "Password must be at least 6 characters!")
    String password;

}
