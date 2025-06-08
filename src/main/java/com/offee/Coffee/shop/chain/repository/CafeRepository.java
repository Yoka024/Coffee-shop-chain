package com.offee.Coffee.shop.chain.repository;

import com.offee.Coffee.shop.chain.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long> {
}
