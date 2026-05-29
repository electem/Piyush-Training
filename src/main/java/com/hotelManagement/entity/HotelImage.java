package com.hotelManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "hotel_images")
@Data
public class HotelImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", nullable = false, length = 1000)
    private String imageUrl;

    @Column(name = "image_name", nullable = false)
    private String imageName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Many images belong to one hotel
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
