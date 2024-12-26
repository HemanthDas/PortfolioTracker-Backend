package org.example.portfoliomanager.controller;

import org.example.portfoliomanager.dto.ResponseAPI;
import org.example.portfoliomanager.models.Stock;
import org.example.portfoliomanager.models.User;
import org.example.portfoliomanager.service.PortfolioService;
import org.example.portfoliomanager.service.StockService;
import org.example.portfoliomanager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin
@RequestMapping("/api/portfolio")
public class PortfolioController {
    private final StockService stockService;
    private final PortfolioService portfolioService;
    private final UserService userService;

    public PortfolioController(StockService stockService, PortfolioService portfolioService, UserService userService) {
        this.stockService = stockService;
        this.portfolioService = portfolioService;
        this.userService = userService;
    }

    @PostMapping("/initialize/{username}")
    public ResponseEntity<ResponseAPI<List<Stock>>> initializePortfolio(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            List<Stock> portfolio = portfolioService.initializePortfolio(user);

            for (Stock stock : portfolio) {
                stockService.addStock(user.getId(), stock);
            }

            ResponseAPI<List<Stock>> response = new ResponseAPI<>(true, "Portfolio initialized successfully.", portfolio);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            ResponseAPI<List<Stock>> response = new ResponseAPI<>(false, ex.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception ex) {
            ResponseAPI<List<Stock>> response = new ResponseAPI<>(false, "An unexpected error occurred: " + ex.getMessage(), null);
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/value/{userId}")
    public ResponseEntity<ResponseAPI<Double>> getPortfolioValue(@PathVariable Long userId) {
        try {
            Double value = stockService.calculatePortfolioValue(userId);
            ResponseAPI<Double> response = new ResponseAPI<>(true, "Portfolio value calculated successfully.", value);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            ResponseAPI<Double> response = new ResponseAPI<>(false, ex.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception ex) {
            ResponseAPI<Double> response = new ResponseAPI<>(false, "An unexpected error occurred: " + ex.getMessage(), null);
            return ResponseEntity.status(500).body(response);
        }
    }
}

