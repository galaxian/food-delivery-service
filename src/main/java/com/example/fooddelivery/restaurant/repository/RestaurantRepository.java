package com.example.fooddelivery.restaurant.repository;

import java.util.List;
import java.util.Optional;

import com.example.fooddelivery.restaurant.domain.City;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.domain.State;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	Optional<Restaurant> findByName(String name);

	// todo 조건에 따른 분기 필요
	@Query(value = "select distinct r from Restaurant r "
		+ "where r.name like %:keyword% "
		+ "and r.restaurantAddress.state = :state "
		+ "and r.restaurantAddress.city = :city ")
	List<Restaurant> findAllByKeywordAndAddress(String keyword, State state, City city);
}
