package com.offee.Coffee.shop.chain.controller;

import com.offee.Coffee.shop.chain.dto.MenuItemRequest;
import com.offee.Coffee.shop.chain.entity.MenuItem;
import com.offee.Coffee.shop.chain.service.MenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/menu_items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("/{cafeId}")
    public ResponseEntity<List<MenuItem>> getMenuByCafe(@PathVariable Long cafeId) {
        List<MenuItem> menuItems = menuItemService.getMenuByCafeId(cafeId);
        return ResponseEntity.ok(menuItems);
    }

    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItemRequest request) {
        MenuItem savedMenuItem = menuItemService.addMenuItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMenuItem);
    }
}
