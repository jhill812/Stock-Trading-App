package com.example.demo.controller;

import com.example.demo.dto.PortfolioDTO;
import com.example.demo.model.Portfolio;
import com.example.demo.model.User;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.repository.UserRepository;
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

    // View Portfolio
    @GetMapping("/{username}")
    public List<PortfolioDTO> viewPortfolio(@PathVariable String username) {
    	User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }
        return portfolioRepository.findByUser(user).stream()
                .map(portfolio -> new PortfolioDTO(portfolio.getStockSymbol(), portfolio.getQuantity()))
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
}
