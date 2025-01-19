package com.example.demo.service;

import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StockPriceService {

    @Value("${FINNHUB_API_KEY}")
    private String apiKey;

    private final String BASE_URL = "https://finnhub.io/api/v1/quote";

    private final RestTemplate restTemplate;

    public StockPriceService() {
        this.restTemplate = new RestTemplate();
        System.out.println("Resolved API Key: " + apiKey);
    }

    public double getStockPrice(String symbol) {
        // Use DefaultUriBuilderFactory to build the URL
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BASE_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);
        String url = factory.builder()
                .queryParam("symbol", symbol)
                .queryParam("token", apiKey)
                .build()
                .toString();

        try {
            // Fetch JSON response from Finnhub.io
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("API Response: " + response);

            // Extract the "current price" from the response
            return extractPriceFromResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching stock price for " + symbol, e);
        }
    }

    private double extractPriceFromResponse(String response) {
        // Extract the "c" (current price) field using simple string parsing
        if (response.contains("\"c\":")) {
            String currentPrice = response.split("\"c\":")[1].split(",")[0];
            return Double.parseDouble(currentPrice);
        }
        throw new RuntimeException("Current price not found in API response");
    }
}
