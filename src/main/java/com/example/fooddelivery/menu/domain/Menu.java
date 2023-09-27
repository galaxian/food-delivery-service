package com.example.fooddelivery.menu.domain;

import com.example.fooddelivery.common.TimeStamped;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "menus")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Menu extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "describes", length = 200, nullable = false)
    private String describe;

    @Column(name = "is_display", nullable = false)
    private boolean isDisplay;

    @Column(name = "menu_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MenuStatus menuStatus;

    @ManyToOne
    @JoinColumn(name = "restuarant_id")
    private Restaurant restaurant;

    private Menu(String name, int price, String describe, Restaurant restaurant) {
        validateMenu(price);
        this.name = name;
        this.price = price;
        this.describe = describe;
        this.isDisplay = true;
        this.menuStatus = MenuStatus.SALE;
        this.restaurant = restaurant;
    }

    public void updateMenu(String name, int price, String describe) {
        validateMenu(price);
        this.name = name;
        this.price = price;
        this.describe = describe;
    }

    public static Menu createMenu(String name, int price, String describe, Restaurant restaurant) {
        return new Menu(name, price, describe, restaurant);
    }

    private void validateMenu(int price) {
        validateMenuPrice(price);
    }

    private void validateMenuPrice(int price) {
        if (isNegative(price)) {
            throw new IllegalArgumentException("메뉴 가격은 0원 보다 커야 합니다.");
        }
    }

    private boolean isNegative(int price) {
        return price < 0;
    }

    public boolean isFairPrice(int sumFoodPrice) {
        return this.price >= sumFoodPrice;
    }
}
