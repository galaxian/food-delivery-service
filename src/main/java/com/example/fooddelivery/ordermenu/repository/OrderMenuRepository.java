package com.example.fooddelivery.ordermenu.repository;

import com.example.fooddelivery.ordermenu.domain.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {
    List<OrderMenu> findByOrderId(Long id);

    void deleteAllByOrderId(Long orderId);
}
