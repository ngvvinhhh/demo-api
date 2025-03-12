package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String image;
    private String name;
    private String description;

    private double price;

    private String category;

    private String petType;

    private String foodType;

    private LocalDateTime create_at;
    private LocalDateTime update_at;

    @Column(name = "is_delete")
    private boolean isDelete;
}
