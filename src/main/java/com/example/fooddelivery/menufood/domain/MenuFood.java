package com.example.fooddelivery.menufood.domain;

import com.example.fooddelivery.common.TimeStamped;
import com.example.fooddelivery.food.domain.Food;
import com.example.fooddelivery.menu.domain.Menu;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "menu-food")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MenuFood extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    private Food food;

    private MenuFood(int quantity, Menu menu, Food food) {
        this.quantity = quantity;
        this.menu = menu;
        this.food = food;
    }

    public static MenuFood createMenuFood(int quantity, Menu menu, Food food) {
        return new MenuFood(quantity, menu, food);
    }
}