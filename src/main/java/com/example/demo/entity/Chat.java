package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Account user;

    private String avatarRecipient;

    private String nameRecipient;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime create_at;
}
