package com.example.demo.api;

import com.example.demo.service.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin("**")
@SecurityRequirement(name = "api")
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @GetMapping("/create-payment")
    public ResponseEntity<String> createPaymentUrl() {
        try {
            String url = paymentService.createUrl();
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating payment URL: " + e.getMessage());
        }
    }

}
