package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String image;
    String name;
    String description;
    float price;
    LocalDateTime create_at;
    LocalDateTime update_at;
    @Column(name = "is_delete")
    private boolean isDelete;
}
