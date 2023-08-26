package com.example.fooddelivery.menu.domain;

import com.example.fooddelivery.common.TimeStamped;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "menus")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Menu extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "describes", nullable = false)
    private String describe;

    @Column(name = "is_display", nullable = false)
    private boolean isDisplay;

    @Column(name = "menu_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MenuStatus menuStatus;

    private Menu(String name, int price, String describe) {
        this.name = name;
        this.price = price;
        this.describe = describe;
    }

    public static Menu createMenu(String name, int price, String describe) {
        return new Menu(name, price, describe);
    }
}
