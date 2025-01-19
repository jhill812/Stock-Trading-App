package com.example.demo.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PortfolioDTO {
    private String stockSymbol;
    private int quantity;
    private double currentPrice; // field for live price
    private double totalValue;   // field for total value (quantity * current price)

    public PortfolioDTO(String stockSymbol, int quantity, double currentPrice, double totalValue) {
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.currentPrice = currentPrice;
        this.totalValue = totalValue;
        this.totalValue = round(totalValue, 2);
    }

    // Getters and setters
    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }
    
    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
