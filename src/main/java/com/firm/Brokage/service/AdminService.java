package com.firm.Brokage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firm.Brokage.entity.Asset;
import com.firm.Brokage.entity.Order;
import com.firm.Brokage.repository.AssetRepository;
import com.firm.Brokage.repository.OrderRepository;

@Service
public class AdminService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AssetRepository assetRepository;

    public void matchPendingOrder(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId).orElseThrow();

        if (!"PENDING".equals(order.getStatus())) {
            throw new Exception("Only pending orders can be matched");
        }

        // Fetch the customer's TRY asset and the stock asset
        Asset tryAsset = assetRepository.findByCustomerAndAssetName(order.getCustomer(), "TRY").orElseThrow();
        Asset stockAsset = assetRepository.findByCustomerAndAssetName(order.getCustomer(), order.getAssetName())
                .orElse(new Asset(order.getCustomer(), order.getAssetName(), 0, 0));

        if ("BUY".equals(order.getOrderSide())) {
            stockAsset.setSize(stockAsset.getSize() + order.getSize());
            stockAsset.setUsableSize(stockAsset.getUsableSize() + order.getSize());
            tryAsset.setUsableSize((int) (tryAsset.getUsableSize() - (order.getSize() * order.getPrice())));
        } else if ("SELL".equals(order.getOrderSide())) {
            stockAsset.setSize(stockAsset.getSize() - order.getSize());
            tryAsset.setUsableSize((int) (tryAsset.getUsableSize() + (order.getSize() * order.getPrice())));
        }

        // Save the updated assets
        assetRepository.save(stockAsset);
        assetRepository.save(tryAsset);

        // Mark the order as MATCHED
        order.setStatus("MATCHED");
        orderRepository.save(order);
    }
}

