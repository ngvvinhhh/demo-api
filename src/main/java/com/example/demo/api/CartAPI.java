package com.example.demo.api;

import com.example.demo.entity.Cart;
import com.example.demo.model.CartResponse;
import com.example.demo.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/cart")
@SecurityRequirement(name = "api")
public class CartAPI {
    @Autowired
    private CartService cartService;
    @GetMapping

    public ResponseEntity<CartResponse> getCartByAccount() {
        CartResponse cart = cartService.getCartByAccount();
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.addToCart(productId, quantity);
        return ResponseEntity.ok("Product added to cart successfully");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeCart(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.removeFromCart(productId, quantity);
        return ResponseEntity.ok("Cart removed successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCart(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.updateCart(productId, quantity);
        return ResponseEntity.ok("Cart updated successfully");
    }
}
