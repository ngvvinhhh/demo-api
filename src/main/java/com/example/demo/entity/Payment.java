package com.example.demo.entity;

import com.example.demo.entity.Enum.PaymentStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatusEnum status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
