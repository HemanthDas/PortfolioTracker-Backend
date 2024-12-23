package org.example.portfoliomanager.service;

import org.example.portfoliomanager.models.Stock;
import org.example.portfoliomanager.models.User;
import org.example.portfoliomanager.repositories.StockRepository;
import org.example.portfoliomanager.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final StockPriceService stockPriceService;
    private final UserRepository userRepository;

    public StockService(StockRepository stockRepository, StockPriceService stockPriceService, UserRepository userRepository) {
        this.stockRepository = stockRepository;
        this.stockPriceService = stockPriceService;
        this.userRepository = userRepository;
    }

    public Stock addStock(Long userId, Stock stock) {
        // Find the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Check if the user already owns the stock by ticker
        Stock existingStock = stockRepository.findByUserIdAndTicker(userId, stock.getTicker());

        if (existingStock != null) {
            // If the stock exists, update the quantity
            existingStock.setQuantity(existingStock.getQuantity() + stock.getQuantity());
            existingStock.setBuyPrice(stock.getBuyPrice());  // Update buy price if needed
            return stockRepository.save(existingStock);
        } else {
            // Set the user for the new stock
            stock.setUser(user);
            return stockRepository.save(stock);
        }
    }


    public Stock updateStock(Long id, Stock stock) {
        try {
            Stock existingStock = stockRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Stock not found with ID: " + id));
            existingStock.setName(stock.getName());
            existingStock.setTicker(stock.getTicker());
            existingStock.setQuantity(stock.getQuantity());
            existingStock.setBuyPrice(stock.getBuyPrice());
            return stockRepository.save(existingStock);
        } catch (RuntimeException ex) {
            throw new RuntimeException("Error updating stock: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while updating stock.", ex);
        }
    }

    public Double calculatePortfolioValue(Long userId) {
        try {
            List<Stock> stocks = stockRepository.findByUserId(userId);
            if (stocks.isEmpty()) {
                throw new RuntimeException("No stocks found for user ID: " + userId);
            }
            double totalValue = 0.0;
            for (Stock stock : stocks) {
                try {
                    System.out.println(stock.getTicker());
                    Double currentPrice = stockPriceService.getStockPrice(stock.getTicker());
                    System.out.println(currentPrice);
                    totalValue += currentPrice * stock.getQuantity();
                } catch (Exception ex) {
                    System.err.println("Error fetching price for stock ticker " + stock.getTicker() + ": " + ex.getMessage());
                }
            }
            return totalValue;
        } catch (RuntimeException ex) {
            throw new RuntimeException("Error calculating portfolio value: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while calculating portfolio value.", ex);
        }
    }

    public void deleteStock(Long id) {
        try {
            if (!stockRepository.existsById(id)) {
                throw new RuntimeException("Stock not found with ID: " + id);
            }
            stockRepository.deleteById(id);
        } catch (RuntimeException ex) {
            throw new RuntimeException("Error deleting stock: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while deleting stock.", ex);
        }
    }

    public Stock getStocksById(Long id) {
        try {
            return stockRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Stock with ID " + id + " not found."));
        } catch (RuntimeException ex) {
            throw new RuntimeException("Error fetching stock by ID: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while fetching stock by ID.", ex);
        }
    }

    public List<Stock> getStocksByUser(Long userId) {
        try {
            List<Stock> stocks = stockRepository.findByUserId(userId);
            if (stocks.isEmpty()) {
                throw new RuntimeException("No stocks found for user ID: " + userId);
            }
            return stocks;
        } catch (RuntimeException ex) {
            throw new RuntimeException("Error fetching stocks for user ID: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while fetching stocks for user.", ex);
        }
    }

}

