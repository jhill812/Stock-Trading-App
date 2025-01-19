package com.example.demo.controller;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.model.Lot;
import com.example.demo.model.Transaction;
import com.example.demo.model.User;
import com.example.demo.repository.LotRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.controller.StockController;
import com.example.demo.service.StockPriceService;
import com.example.demo.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private LotRepository lotRepository; 
    
    @Autowired
    private StockPriceService stockPriceService;
    
    @PostMapping("/buy")
    public String buyStock(@RequestParam String username, @RequestBody Transaction transaction) {
        // Validate user
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

        // Create a new Lot
        Lot lot = new Lot();
        lot.setStockSymbol(transaction.getStockSymbol());
        lot.setPricePerShare(price);
        lot.setQuantity(transaction.getQuantity());
        lot.setPurchaseDate(LocalDateTime.now());
        lot.setUser(user);

        lotRepository.save(lot); // Save the lot

        // Save the transaction
        transaction.setUser(user);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setPrice(price); // Store the price used
        transaction.setBuy(true);
        transactionRepository.save(transaction);

        return "Stock purchased and lot recorded successfully!";
    }
}
