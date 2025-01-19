package com.example.demo.controller;

import com.example.demo.dto.PortfolioDTO;
import com.example.demo.model.Portfolio;
import com.example.demo.model.Transaction;
import com.example.demo.model.User;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.StockPriceService;
import com.example.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private StockPriceService stockPriceService;

    // Add Stock to Portfolio
    @PostMapping("/add")
    public String addStock(@RequestParam String username, @RequestBody Portfolio portfolio) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "User not found!";
        }
        portfolio.setUser(user);
        portfolioRepository.save(portfolio);
        return "Stock added to portfolio!";
    }

    // View Portfolio with Live Prices
    @GetMapping("/{username}")
    public List<PortfolioDTO> viewPortfolio(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }

        return portfolioRepository.findByUser(user).stream()
                .map(portfolio -> {
                    double currentPrice = stockPriceService.getStockPrice(portfolio.getStockSymbol());
                    double totalValue = currentPrice * portfolio.getQuantity();
                    return new PortfolioDTO(portfolio.getStockSymbol(), portfolio.getQuantity(), currentPrice, totalValue);
                })
                .toList();
    }

    // Delete Stock from Portfolio
    @DeleteMapping("/{id}")
    public String deleteStock(@PathVariable Long id) {
    	 if (!portfolioRepository.existsById(id)) {
    	        throw new IllegalArgumentException("Stock not found in portfolio!");
    	    }
	    portfolioRepository.deleteById(id);
	    return "Stock removed from portfolio!";
    }
    
    @GetMapping("/test")
    public String test() {
        return "Portfolio test endpoint is working!";
    }
    
    @PostMapping("/buy")
    public String buyStock(@RequestParam String username, @RequestBody Transaction buyRequest) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "User not found!";
        }

        // Check if the stock already exists in the portfolio
        Portfolio portfolio = portfolioRepository.findByUserAndStockSymbol(user, buyRequest.getStockSymbol());
        if (portfolio == null) {
            // Create a new portfolio entry if the stock doesn't exist
            portfolio = new Portfolio();
            portfolio.setUser(user);
            portfolio.setStockSymbol(buyRequest.getStockSymbol());
            portfolio.setQuantity(buyRequest.getQuantity());
        } else {
            // Update the existing portfolio entry by adding the quantity
            portfolio.setQuantity(portfolio.getQuantity() + buyRequest.getQuantity());
        }

        // Save the portfolio changes
        portfolioRepository.save(portfolio);

        // Record the buy transaction
        buyRequest.setUser(user);
        buyRequest.setTransactionDate(LocalDateTime.now());
        buyRequest.setBuy(true); // Mark this as a buy transaction
        transactionRepository.save(buyRequest);

        return "Buy transaction recorded successfully!";
    }

    @PostMapping("/sell")
    public String sellStock(@RequestParam String username, @RequestBody Transaction sellRequest) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "User not found!";
        }

        // Check if the stock exists in the portfolio
        Portfolio portfolio = portfolioRepository.findByUserAndStockSymbol(user, sellRequest.getStockSymbol());
        if (portfolio == null) {
            return "Stock not found in portfolio!";
        }

        // Validate quantity
        if (portfolio.getQuantity() < sellRequest.getQuantity()) {
            return "Insufficient stock quantity to sell!";
        }

        // Update the portfolio
        int remainingQuantity = portfolio.getQuantity() - sellRequest.getQuantity();
        if (remainingQuantity == 0) {
            portfolioRepository.delete(portfolio); // Remove the stock if all shares are sold
        } else {
            portfolio.setQuantity(remainingQuantity);
            portfolioRepository.save(portfolio); // Save updated quantity
        }

        // Record the sell transaction
        sellRequest.setUser(user);
        sellRequest.setTransactionDate(LocalDateTime.now());
        sellRequest.setBuy(false); // Mark this as a sell transaction
        transactionRepository.save(sellRequest);

        return "Sell transaction recorded successfully!";
    }

}
