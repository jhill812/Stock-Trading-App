package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.demo")
public class StockTradingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockTradingAppApplication.class, args);
	}

}
