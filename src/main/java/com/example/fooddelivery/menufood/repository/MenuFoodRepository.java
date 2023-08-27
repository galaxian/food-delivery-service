package com.example.fooddelivery.menufood.repository;

import com.example.fooddelivery.menufood.domain.MenuFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuFoodRepository extends JpaRepository<MenuFood, Long> {
    List<MenuFood> findByMenuId(Long id);
}
