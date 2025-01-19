package com.example.demo.controller;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.model.Lot;
import com.example.demo.model.Portfolio;
import com.example.demo.model.Transaction;
import com.example.demo.model.User;
import com.example.demo.repository.LotRepository;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LotRepository lotRepository;

    @Autowired
    private StockPriceService stockPriceService;

    // Buy Stock
    @PostMapping("/buy")
    public String buyStock(@RequestParam String username, @RequestBody Transaction transaction) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }

        // Fetch live price if not provided
        double price = transaction.getPrice();
        if (price <= 0) {
            price = stockPriceService.getStockPrice(transaction.getStockSymbol());
            System.out.println("Fetched live price for " + transaction.getStockSymbol() + ": " + price);
        }

        // Update Portfolio
        Portfolio portfolio = portfolioRepository.findByUserAndStockSymbol(user, transaction.getStockSymbol());
        if (portfolio == null) {
            portfolio = new Portfolio();
            portfolio.setUser(user);
            portfolio.setStockSymbol(transaction.getStockSymbol());
            portfolio.setQuantity(transaction.getQuantity());
        } else {
            portfolio.setQuantity(portfolio.getQuantity() + transaction.getQuantity());
        }
        portfolioRepository.save(portfolio);

        // Create a new Lot
        Lot lot = new Lot();
        lot.setStockSymbol(transaction.getStockSymbol());
        lot.setPricePerShare(price);
        lot.setQuantity(transaction.getQuantity());
        lot.setPurchaseDate(LocalDateTime.now());
        lot.setUser(user);
        lotRepository.save(lot);

        // Save the transaction
        transaction.setUser(user);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setPrice(price);
        transaction.setBuy(true);
        transactionRepository.save(transaction);

        return "Stock purchased and portfolio updated successfully!";
    }

    // Sell Stock
    @PostMapping("/sell")
    public String sellStock(@RequestParam String username, @RequestBody Transaction transaction) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "User not found!";
        }

        Portfolio portfolio = portfolioRepository.findByUserAndStockSymbol(user, transaction.getStockSymbol());
        if (portfolio == null || portfolio.getQuantity() < transaction.getQuantity()) {
            return "Insufficient stock quantity to sell!";
        }

        // Deduct quantity from portfolio
        int remainingQuantity = portfolio.getQuantity() - transaction.getQuantity();
        if (remainingQuantity == 0) {
            portfolioRepository.delete(portfolio);
        } else {
            portfolio.setQuantity(remainingQuantity);
            portfolioRepository.save(portfolio);
        }

        // Deduct shares from Lots (FIFO logic can be added here later)

        // Save the transaction
        transaction.setUser(user);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setPrice(stockPriceService.getStockPrice(transaction.getStockSymbol())); // Current price
        transaction.setBuy(false);
        transactionRepository.save(transaction);

        return "Stock sold and portfolio updated successfully!";
    }
}
