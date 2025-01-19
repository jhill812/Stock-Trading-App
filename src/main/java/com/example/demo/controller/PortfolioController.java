package com.example.demo.controller;

import com.example.demo.dto.PortfolioDTO;
import com.example.demo.model.Portfolio;
import com.example.demo.model.User;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockPriceService stockPriceService;

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
}
