package com.example.fooddelivery.food.repository;

import java.util.List;

import com.example.fooddelivery.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
	List<Food> findAllByRestaurantId(Long restaurantId);
}
