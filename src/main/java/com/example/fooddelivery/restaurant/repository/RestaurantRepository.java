package com.example.fooddelivery.restaurant.repository;

import java.util.Optional;

import com.example.fooddelivery.restaurant.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	Optional<Restaurant> findByName(String name);
}
