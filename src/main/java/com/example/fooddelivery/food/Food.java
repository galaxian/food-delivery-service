package com.example.fooddelivery.food;

import com.example.fooddelivery.common.TimeStamped;
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

    public static Food createFood(String name, int price) {
        return new Food(name, price);
    }

    private Food(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public void updateFood(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
