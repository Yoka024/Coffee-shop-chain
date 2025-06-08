package com.offee.Coffee.shop.chain.controller;

import com.offee.Coffee.shop.chain.entity.Order;
import com.offee.Coffee.shop.chain.entity.MenuItem;
import com.offee.Coffee.shop.chain.entity.Cafe;
import com.offee.Coffee.shop.chain.service.OrderService;
import com.offee.Coffee.shop.chain.repository.CafeRepository;
import com.offee.Coffee.shop.chain.repository.MenuItemRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final MenuItemRepository menuItemRepository;
    private final CafeRepository cafeRepository;

    public OrderController(OrderService orderService,
                           MenuItemRepository menuItemRepository,
                           CafeRepository cafeRepository) {
        this.orderService = orderService;
        this.menuItemRepository = menuItemRepository;
        this.cafeRepository = cafeRepository;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        Optional<Cafe> cafeOptional = cafeRepository.findById(request.getCafeId());
        if (cafeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<MenuItem> items = menuItemRepository.findAllById(request.getItemIds());

        Order order = new Order();
        order.setCafe(cafeOptional.get());
        order.setCustomerName(request.getCustomerName());
        order.setItems(items);

        Order savedOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @GetMapping("/by-customer")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@RequestParam String customerName) {
        List<Order> orders = orderService.getOrdersByCustomer(customerName);
        return ResponseEntity.ok(orders);
    }

    public static class OrderRequest {
        private Long cafeId;
        private String customerName;
        private List<Long> itemIds;

        public Long getCafeId() {
            return cafeId;
        }

        public void setCafeId(Long cafeId) {
            this.cafeId = cafeId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public List<Long> getItemIds() {
            return itemIds;
        }

        public void setItemIds(List<Long> itemIds) {
            this.itemIds = itemIds;
        }
    }
}
