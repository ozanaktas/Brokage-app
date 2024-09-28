package com.firm.Brokage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.firm.Brokage.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/match-order/{orderId}")
    public ResponseEntity<Void> matchOrder(@PathVariable Long orderId) throws Exception {
        adminService.matchPendingOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}

