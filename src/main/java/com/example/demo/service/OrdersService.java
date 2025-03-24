package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Orders;
import com.example.demo.model.OrdersResponse;
import com.example.demo.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private AuthenticationService authenticationService;

    public List<OrdersResponse> getAllOrders() {
        List<Orders> orders = ordersRepository.findAll();
        return orders.stream().map(this::mapToOrdersResponse).toList();
    }

    public List<OrdersResponse> getOrdersByCurrentAccount() {
        Account currentAccount = authenticationService.getCurrentAccount();
        List<Orders> orders = ordersRepository.findByUser(currentAccount);
        return orders.stream().map(this::mapToOrdersResponse).toList();
    }

    private OrdersResponse mapToOrdersResponse(Orders order) {
        OrdersResponse response = new OrdersResponse();
        response.setId(order.getOrderId());
        response.setCustomerName(order.getUser().getName());
        response.setCustomerEmail(order.getUser().getEmail());
        response.setCustomerAddress(order.getUser().getAddress());
        response.setCustomerPhone(order.getUser().getPhone());
        response.setTotalAmount(order.getTotalAmount());

        if (!order.getOrderItems().isEmpty()) {
            OrderItem firstItem = order.getOrderItems().get(0);
            response.setProductName(firstItem.getProduct().getName());
            response.setProductImage(firstItem.getProduct().getImage());
            response.setQuantity(firstItem.getQuantity());
        }

        return response;
    }
}
