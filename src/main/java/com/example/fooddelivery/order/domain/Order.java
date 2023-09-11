package com.example.fooddelivery.order.domain;

import com.example.fooddelivery.common.TimeStamped;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Order extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "order_time")
    private LocalDateTime orderTime;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private Order(LocalDateTime orderTime, Restaurant restaurant) {
        this.orderStatus = OrderStatus.WAITING;
        this.orderTime = orderTime;
        this.restaurant = restaurant;
    }

    public static Order createOrder(LocalDateTime orderTime, Restaurant restaurant) {
        return new Order(orderTime, restaurant);
    }
}
