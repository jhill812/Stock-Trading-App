package com.example.demo.dto;

import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private String stockSymbol;
    private int quantity;
    private double price;
    private boolean isBuy;
    private LocalDateTime transactionDate;
    private String username; // Include only the username, not the password

    public TransactionDTO(Long id, String stockSymbol, int quantity, double price, boolean isBuy, LocalDateTime transactionDate, String username) {
        this.id = id;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.price = price;
        this.isBuy = isBuy;
        this.transactionDate = transactionDate;
        this.username = username;
    }
    
    // Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isBuy() {
		return isBuy;
	}

	public void setBuy(boolean isBuy) {
		this.isBuy = isBuy;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

   
    
}
