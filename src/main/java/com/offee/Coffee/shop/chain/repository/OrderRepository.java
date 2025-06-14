package com.offee.Coffee.shop.chain.repository;

import com.offee.Coffee.shop.chain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}