package com.example.fooddelivery.menu.repository;

import java.util.List;

import com.example.fooddelivery.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
	List<Menu> findAllByRestaurantId(Long restaurantId);
}
