package org.example.portfoliomanager.service;

import org.example.portfoliomanager.models.Stock;
import org.example.portfoliomanager.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class PortfolioService {
    private static final List<String> AVAILABLE_STOCKS = List.of("AAPL", "MSFT", "GOOG", "AMZN", "TSLA");
    private final StockPriceService stockPriceService;
    public PortfolioService(StockPriceService stockPriceService ) {
        this.stockPriceService = stockPriceService;
    }
    public List<Stock> initializePortfolio(User user) {
        // Validate the input user
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (user.getId() == null || user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Invalid user details. User ID and username are required.");
        }

        try {
            Random random = new Random();
            List<Stock> portfolio = new ArrayList<>();

            // Track added tickers to avoid duplicating stocks in the portfolio
            Set<String> addedTickers = new HashSet<>();

            for (int i = 0; i < 5; i++) {
                String randomTicker = AVAILABLE_STOCKS.get(random.nextInt(AVAILABLE_STOCKS.size()));

                // Ensure the stock ticker is not added again
                while (addedTickers.contains(randomTicker)) {
                    randomTicker = AVAILABLE_STOCKS.get(random.nextInt(AVAILABLE_STOCKS.size()));
                }

                addedTickers.add(randomTicker); // Add ticker to the set

                Stock stock = new Stock();
                stock.setName("Stock " + randomTicker);
                stock.setTicker(randomTicker);
                stock.setQuantity(1);  // Default to 1, you can adjust as needed
                Double currentPrice = stockPriceService.getStockPrice(randomTicker);
                if (currentPrice == 0.0) {
                    throw new RuntimeException("Failed to fetch stock price for ticker: " + randomTicker);
                }
                stock.setBuyPrice(currentPrice);  // Set the fetched price
                stock.setUser(user);  // Associate with the user

                portfolio.add(stock);
            }
            return portfolio;

        } catch (Exception ex) {
            // Log the error (if a logging framework is integrated)
            throw new RuntimeException("Failed to initialize the portfolio: " + ex.getMessage(), ex);
        }
    }
}
