package com.example.demo.controller;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.model.Transaction;
import com.example.demo.model.User;
import com.example.demo.repository.TransactionRepository;
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

    // Record a transaction
    @PostMapping("/record")
    public String recordTransaction(@RequestParam String username, @RequestBody Transaction transaction) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }
        
      //**FOR TESTING//
        System.out.println("Transaction isBuy: " + transaction.isBuy());

        transaction.setUser(user);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
        
        return "Transaction recorded successfully!";        
    }

    @GetMapping("/{username}")
    public List<TransactionDTO> viewTransactionHistory(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }

        return transactionRepository.findByUser(user).stream()
                .map(transaction -> new TransactionDTO(
                    transaction.getId(),
                    transaction.getStockSymbol(),
                    transaction.getQuantity(),
                    transaction.getPrice(),
                    transaction.isBuy(),
                    transaction.getTransactionDate(),
                    user.getUsername() // Include only the username
                ))
                .toList();
    }
}
