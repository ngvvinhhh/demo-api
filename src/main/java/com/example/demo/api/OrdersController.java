package com.example.demo.api;

import com.example.demo.model.OrdersResponse;
import com.example.demo.service.OrdersService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @GetMapping("/all")
    public List<OrdersResponse> getAllOrders() {
        return ordersService.getAllOrders();
    }

    @GetMapping("/my")
    public List<OrdersResponse> getOrdersByCurrentAccount() {
        return ordersService.getOrdersByCurrentAccount();
    }
}