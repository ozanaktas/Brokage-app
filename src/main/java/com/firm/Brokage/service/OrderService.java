package com.firm.Brokage.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firm.Brokage.entity.Asset;
import com.firm.Brokage.entity.Customer;
import com.firm.Brokage.entity.Order;
import com.firm.Brokage.repository.AssetRepository;
import com.firm.Brokage.repository.CustomerRepository;
import com.firm.Brokage.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private AssetRepository assetRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    public List<Order> getOrdersForCustomer(String email) {
        Customer customer = customerRepository.findByEmail(email).orElseThrow();

        return orderRepository.findByCustomer(customer);
    }    
    
    public Order createOrder(Long customerId, String assetName, String orderSide, int size, double price) throws Exception {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        
        // Check if customer has enough TRY balance before creating order
        Asset tryAsset = assetRepository.findByCustomerAndAssetName(customer, "TRY").orElseThrow();
        
        if (tryAsset.getUsableSize() < size * price) {
            throw new Exception("Not enough TRY to place the order");
        }
        
        // Create order and update TRY asset
        Order order = new Order();
        order.setCustomer(customer);
        order.setAssetName(assetName);
        order.setOrderSide(orderSide);
        order.setSize(size);
        order.setPrice(price);
        order.setStatus("PENDING");
        order.setCreateDate(LocalDate.now());
        
        orderRepository.save(order);
        
        // Update TRY asset's usable size
        tryAsset.setUsableSize((int) (tryAsset.getUsableSize() - (size * price)));
        assetRepository.save(tryAsset);
        
        return order;
    }

    public void cancelOrder(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId).orElseThrow();
        
        if (!"PENDING".equals(order.getStatus())) {
            throw new Exception("Only pending orders can be canceled");
        }

        Asset tryAsset = assetRepository.findByCustomerAndAssetName(order.getCustomer(), "TRY").orElseThrow();
        tryAsset.setUsableSize((int) (tryAsset.getUsableSize() + (order.getSize() * order.getPrice())));
        assetRepository.save(tryAsset);

        order.setStatus("CANCELED");
        orderRepository.save(order);
    }
}

