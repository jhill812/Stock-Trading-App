package com.example.demo.repository;

import com.example.demo.model.Lot;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LotRepository extends JpaRepository<Lot, Long> {
    List<Lot> findByUserAndStockSymbol(User user, String stockSymbol);
}
