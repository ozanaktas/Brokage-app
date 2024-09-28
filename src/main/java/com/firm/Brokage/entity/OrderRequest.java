package com.firm.Brokage.entity;

import com.firm.Brokage.constant.OrderSide;

public class OrderRequest {

    private Long customerId;  // The customer placing the order
    private String assetName; // The name of the stock (asset)
    private OrderSide side;   // BUY or SELL (order side)
    private int size;         // Number of shares
    private double price;     // Price per share

    // Default constructor
    public OrderRequest() {
    }

    // Parameterized constructor
    public OrderRequest(Long customerId, String assetName, OrderSide side, int size, double price) {
        this.customerId = customerId;
        this.assetName = assetName;
        this.side = side;
        this.size = size;
        this.price = price;
    }

    // Getters and setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public OrderSide getSide() {
        return side;
    }

    public void setSide(OrderSide side) {
        this.side = side;
    }

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
    
    
}

