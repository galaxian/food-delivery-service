package com.example.fooddelivery.food.domain;

import com.example.fooddelivery.common.TimeStamped;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "food")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Food extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public static Food createFood(String name, int price, Restaurant restaurant) {
        return new Food(name, price, restaurant);
    }

    private Food(String name, int price, Restaurant restaurant) {
        if (price <= 0) {
            throw new IllegalArgumentException("음식 가격은 0원 보다 커야 합니다.");
        }
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
    }

    public void updateFood(String name, int price) {
        if (price <= 0) {
            throw new IllegalArgumentException("음식 가격은 0원 보다 커야 합니다.");
        }
        this.name = name;
        this.price = price;
    }
}
