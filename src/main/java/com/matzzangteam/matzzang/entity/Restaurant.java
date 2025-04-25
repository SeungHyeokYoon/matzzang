package com.matzzangteam.matzzang.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurants")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantCategory category;

    private Double latitude;

    private Double longitude;

    private String imageUrl;


    public enum RestaurantCategory {
        KOREAN, CHINESE, JAPANESE, WESTERN, DESSERT, CAFE, ETC
    }

}
