package com.example.fooddelivery.order.repository;

import java.util.List;

import com.example.fooddelivery.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findAllByRestaurantId(Long restaurantId);
}
