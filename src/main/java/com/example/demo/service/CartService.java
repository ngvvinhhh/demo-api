package com.example.demo.service;


import com.example.demo.entity.Account;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Food;
import com.example.demo.exception.FoodNotFound;
import com.example.demo.model.CartItemResponse;
import com.example.demo.model.CartResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthenticationService authenticationService;

    public CartResponse getCartByAccount() {
        Account user = authenticationService.getCurrentAccount();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new FoodNotFound("Cart not found"));

        // Lấy tất cả cart items
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        // Chuyển đổi sang CartItemResponse
        List<CartItemResponse> itemResponses = cartItems.stream().map(item -> {
            Food product = item.getProduct();
            double totalAmount = product.getPrice() * item.getQuantity();
            return new CartItemResponse(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImage(),
                    item.getQuantity(),
                    totalAmount
            );
        }).collect(Collectors.toList());

        // Tính tổng tiền
        double totalAmount = itemResponses.stream()
                .mapToDouble(CartItemResponse::getTotalAmount)
                .sum();

        return new CartResponse(cart.getCartId(), itemResponses, totalAmount);
    }

    public void addToCart(Long productId, int quantity) {
        Account user = authenticationService.getCurrentAccount();
        Optional<Food> productOptional = foodRepository.findById(productId);

        if (productOptional.isEmpty()) {
            throw new FoodNotFound("Product not found");
        }
        Food product = productOptional.get();

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setCreateAt(LocalDateTime.now());
            return cartRepository.save(newCart);
        });

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product).orElseGet(() -> {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(0);
            newItem.setCreate_at(LocalDateTime.now());
            return newItem;
        });

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItemRepository.save(cartItem);
    }

    public void removeFromCart(Long productId, int quantity) {
        Account user = authenticationService.getCurrentAccount();
        Optional<Food> productOptional = foodRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new FoodNotFound("Product not found");
        }
        Food product = productOptional.get();

        Optional<Cart> cartOptional = cartRepository.findByUser(user);
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            Optional<CartItem> cartItemOptional = cartItemRepository.findByCartAndProduct(cart, product);
            if (cartItemOptional.isPresent()) {
                CartItem cartItem = cartItemOptional.get();
                if (cartItem.getQuantity() <= quantity) {
                    cartItemRepository.delete(cartItem);
                } else {
                    cartItem.setQuantity(cartItem.getQuantity() - quantity);
                    cartItemRepository.save(cartItem);
                }
            } else {
                throw new FoodNotFound("Product not found in cart");
            }
        }
    }

    public void updateCart(Long productId, int quantity) {
        Account user = authenticationService.getCurrentAccount();
        Optional<Food> productOptional = foodRepository.findById(productId);

        if (productOptional.isEmpty()) {
            throw new FoodNotFound("Product not found");
        }
        Food product = productOptional.get();

        Optional<Cart> cartOptional = cartRepository.findByUser(user);
        if (cartOptional.isEmpty()) {
            throw new FoodNotFound("Cart not found");
        }
        Cart cart = cartOptional.get();

        Optional<CartItem> cartItemOptional = cartItemRepository.findByCartAndProduct(cart, product);
        if (cartItemOptional.isEmpty()) {
            throw new FoodNotFound("Product not found in cart");
        }

        CartItem cartItem = cartItemOptional.get();
        cartItem.setQuantity(quantity);
        cartItem.setUpdate_at(LocalDateTime.now());
        cartItemRepository.save(cartItem);

        cart.setUpdate_at(LocalDateTime.now());
        cartRepository.save(cart);
    }
}