package com.offee.Coffee.shop.chain.service;

import com.offee.Coffee.shop.chain.entity.Cafe;
import com.offee.Coffee.shop.chain.repository.CafeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CafeService {

    private final CafeRepository cafeRepository;

    @Autowired
    public CafeService(CafeRepository cafeRepository) {
        this.cafeRepository = cafeRepository;
    }

    public List<Cafe> getAllCafes() {
        return cafeRepository.findAll();
    }

    public Cafe addCafe(Cafe cafe) {
        return cafeRepository.save(cafe);
    }
}
