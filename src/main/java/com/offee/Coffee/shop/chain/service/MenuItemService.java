package com.offee.Coffee.shop.chain.service;

import com.offee.Coffee.shop.chain.dto.MenuItemRequest;
import com.offee.Coffee.shop.chain.entity.Cafe;
import com.offee.Coffee.shop.chain.entity.MenuItem;
import com.offee.Coffee.shop.chain.repository.CafeRepository;
import com.offee.Coffee.shop.chain.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CafeRepository cafeRepository;

    @Autowired
    public MenuItemService(MenuItemRepository menuItemRepository, CafeRepository cafeRepository) {
        this.menuItemRepository = menuItemRepository;
        this.cafeRepository = cafeRepository;
    }

    public List<MenuItem> getMenuByCafeId(Long cafeId) {
        return menuItemRepository.findAll().stream()
                .filter(item -> item.getCafeId().equals(cafeId))
                .toList();
    }

    public MenuItem addMenuItem(MenuItemRequest request) {
        Cafe cafe = cafeRepository.findById(request.getCafeId())
                .orElseThrow(() -> new RuntimeException("Cafe not found"));

        MenuItem menuItem = new MenuItem();
        menuItem.setCafe(cafe);
        menuItem.setName(request.getName());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());

        return menuItemRepository.save(menuItem);
    }
}