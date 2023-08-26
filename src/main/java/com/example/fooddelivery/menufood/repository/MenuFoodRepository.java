package com.example.fooddelivery.menufood.repository;

import com.example.fooddelivery.menufood.domain.MenuFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuFoodRepository extends JpaRepository<MenuFood, Long> {
}
