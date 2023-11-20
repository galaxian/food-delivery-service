package com.example.fooddelivery.restaurant.domain;

import com.example.fooddelivery.common.TimeStamped;
import com.example.fooddelivery.food.domain.Food;
import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.order.domain.Order;
import com.example.fooddelivery.owner.domain.Owner;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "restaurants")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Restaurant extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "min_price")
    private int minPrice;

    @Column(name = "delivery_fee")
    private int deliveryFee;

    @Embedded
    private Address restaurantAddress;

    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Food> foods = new ArrayList<>();

    public void update(String name, int minPrice, int deliveryFee) {
        this.name = name;
        this.minPrice = minPrice;
        this.deliveryFee = deliveryFee;
    }

    public Restaurant(String name, int minPrice, int deliveryFee, Address address, Owner owner) {
        this.name = name;
        this.minPrice = minPrice;
        this.deliveryFee = deliveryFee;
        this.restaurantAddress = address;
        this.owner = owner;
    }

    public boolean isOwner(String identifier) {
        return Objects.equals(this.owner.getIdentifier(), identifier);
    }
}
