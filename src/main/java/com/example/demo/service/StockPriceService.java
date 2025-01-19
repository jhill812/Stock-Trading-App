package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockPriceService {

    @Value("${finnhub.api_key}")
    private String apiKey;

    private final String BASE_URL = "https://finnhub.io/api/v1/quote";

    private final RestTemplate restTemplate = new RestTemplate();

    @Cacheable(value = "stockPrices", key = "#symbol")
    public double getStockPrice(String symbol) {
        String url = BASE_URL + "?symbol=" + symbol + "&token=" + apiKey;
        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("API Response: " + response);
            return extractPriceFromResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching stock price for " + symbol, e);
        }
    }

    private double extractPriceFromResponse(String response) {
        if (response.contains("\"c\":")) {
            String currentPrice = response.split("\"c\":")[1].split(",")[0];
            return Double.parseDouble(currentPrice);
        }
        throw new RuntimeException("Current price not found in API response");
    }
}
