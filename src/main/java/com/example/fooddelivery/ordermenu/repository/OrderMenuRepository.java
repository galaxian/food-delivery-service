package com.example.fooddelivery.ordermenu.repository;

import com.example.fooddelivery.ordermenu.domain.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {
}
