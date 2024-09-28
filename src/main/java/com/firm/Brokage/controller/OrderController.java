package com.firm.Brokage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.firm.Brokage.entity.Order;
import com.firm.Brokage.entity.OrderRequest;
import com.firm.Brokage.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) throws Exception {
        Order order = orderService.createOrder(orderRequest.getCustomerId(), orderRequest.getAssetName(),
                orderRequest.getSide().name(), orderRequest.getSize(), orderRequest.getPrice());
        return ResponseEntity.ok(order);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) throws Exception {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
    
}

