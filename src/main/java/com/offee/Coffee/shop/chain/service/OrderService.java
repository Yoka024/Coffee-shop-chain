package com.offee.Coffee.shop.chain.service;

import com.offee.Coffee.shop.chain.entity.Order;
import com.offee.Coffee.shop.chain.repository.OrderRepository;
import com.offee.Coffee.shop.chain.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public Order createOrder(Order order) {
        double total = order.getItems().stream()
                .mapToDouble(item -> menuItemRepository.findById(item.getId())
                        .map(i -> i.getPrice())
                        .orElse(0.0))
                .sum();

        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByCustomer(String customerName) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getCustomerName().equalsIgnoreCase(customerName))
                .toList();
    }
}
