package com.example.demo.controller;

import com.example.demo.service.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockPriceService stockPriceService;

    @GetMapping("/price")
    public double getStockPrice(@RequestParam String symbol) {
        return stockPriceService.getStockPrice(symbol);
    }
}
