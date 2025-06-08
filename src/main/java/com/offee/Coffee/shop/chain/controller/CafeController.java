package com.offee.Coffee.shop.chain.controller;

import com.offee.Coffee.shop.chain.entity.Cafe;
import com.offee.Coffee.shop.chain.service.CafeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/cafes")
public class CafeController {

    private final CafeService cafeService;

    public CafeController(CafeService cafeService) {
        this.cafeService = cafeService;
    }

    @GetMapping
    public ResponseEntity<List<Cafe>> getAllCafes() {
        List<Cafe> cafes = cafeService.getAllCafes();
        return ResponseEntity.ok(cafes);
    }

    @PostMapping
    public ResponseEntity<Cafe> addCafe(@RequestBody Cafe cafe) {
        Cafe savedCafe = cafeService.addCafe(cafe);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCafe);
    }
}