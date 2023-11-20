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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "phone_number")
    private String phoneNumber;

    @Embedded
    private DeliveredAddress deliveredAddress;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private Order(Restaurant restaurant, String phoneNumber, DeliveredAddress deliveredAddress) {
        this.orderStatus = OrderStatus.WAITING;
        this.orderTime = LocalDateTime.now();
        this.restaurant = restaurant;
        this.phoneNumber = phoneNumber;
        this.deliveredAddress = deliveredAddress;
    }

    public static Order createOrder(Restaurant restaurant, String phoneNumber, DeliveredAddress deliveredAddress) {
        return new Order(restaurant, phoneNumber, deliveredAddress);
    }
}
