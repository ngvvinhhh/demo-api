package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.entity.Enum.OrderStatus;
import com.example.demo.entity.Enum.PaymentStatusEnum;
import com.example.demo.exception.FoodNotFound;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    CartService cartService;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    OrderItemRepository orderItemRepository;


    public Payment createPayment() {
        Account customer = authenticationService.getCurrentAccount();
        Cart cart = cartRepository.findByUser(customer)
                .orElseThrow(() -> new FoodNotFound("Cart not found for user: " + customer.getId()));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty, cannot create payment");
        }

        Optional<Payment> existingPayment = paymentRepository.findByCart(cart);
        if (existingPayment.isPresent()) {
            throw new RuntimeException("A payment already exists for this cart: " + cart.getCartId());
        }

        Payment payment = new Payment();
        payment.setCreatedAt(LocalDateTime.now());
        payment.setCart(cart);
        payment.setTotal(cart.getCartItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum());
        payment.setStatus(PaymentStatusEnum.PENDING);
        return paymentRepository.save(payment);
    }

    public String createUrl() throws Exception {
        String formattedCreateDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Payment payment = createPayment();
        paymentRepository.save(payment);





        // Tạo URL VNPAY
        String amount = String.valueOf((int) (payment.getTotal() * 100));
        if (amount.equals("0")) {
            throw new IllegalStateException("Payment amount cannot be zero");
        }

        String tmnCode = "6GKPCPYB";
        String secretKey = "V7XPX7YZIC1PFDDZKALSBUZV741S7F0S";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "http://localhost:8080/swagger-ui/index.html#/" + payment.getId();
        String currCode = "VND";

        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_CurrCode", currCode);
        vnpParams.put("vnp_TxnRef", payment.getId().toString());
        vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD: " + payment.getId());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Amount", amount);
        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_CreateDate", formattedCreateDate);
        vnpParams.put("vnp_IpAddr", "128.199.178.23");

        StringBuilder signDataBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            signDataBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            signDataBuilder.append("=");
            signDataBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            signDataBuilder.append("&");
        }
        signDataBuilder.deleteCharAt(signDataBuilder.length() - 1); // Remove last '&'

        String signData = signDataBuilder.toString();
        String signed = generateHMAC(secretKey, signData);

        vnpParams.put("vnp_SecureHash", signed);

        StringBuilder urlBuilder = new StringBuilder(vnpUrl);
        urlBuilder.append("?");
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            urlBuilder.append("=");
            urlBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            urlBuilder.append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1); // Remove last '&'

        Cart cart = payment.getCart();
        List<CartItem> cartItems = cart.getCartItems();
        cart.setDelete(true);
//        Orders order = new Orders();
//        order.setUser(cart.getUser());
//        order.setTotalAmount(payment.getTotal());
//        order.setStatus(OrderStatus.Pending);
//        order.setCreate_at(LocalDateTime.now());
//        order.setUpdate_at(LocalDateTime.now());
//        order.setDelete(false);
//        order = ordersRepository.save(order);
//
//        Orders finalOrder = order;
//        List<OrderItem> orderItems = cart.getCartItems().stream()
//                .map(cartItem -> {
//                    OrderItem item = new OrderItem();
//                    item.setOrder(finalOrder);
//                    item.setProduct(cartItem.getProduct());
//                    item.setQuantity(cartItem.getQuantity());
//                    return item;
//                }).toList();
//        orderItemRepository.saveAll(orderItems);
//        order.setOrderItems(orderItems);
//        ordersRepository.save(order);
//
//        payment.setStatus(PaymentStatusEnum.SUCCESS);
//        paymentRepository.save(payment);
        return urlBuilder.toString();
    }


    private String generateHMAC(String secretKey, String signData) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmacSha512.init(keySpec);
        byte[] hmacBytes = hmacSha512.doFinal(signData.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();
        for (byte b : hmacBytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}