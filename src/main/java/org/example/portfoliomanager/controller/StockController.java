package org.example.portfoliomanager.controller;

import org.example.portfoliomanager.dto.*;
import org.example.portfoliomanager.models.Stock;
import org.example.portfoliomanager.service.StockPriceService;
import org.example.portfoliomanager.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;
    private final StockPriceService stockPriceService;
    public StockController(StockService stockService, StockPriceService stockPriceService) {
        this.stockService = stockService;
        this.stockPriceService = stockPriceService;
    }

    // Add a new stock
    @PostMapping("/{userId}")
    public ResponseEntity<ResponseAPI<StockDTO>> addStock(@PathVariable Long userId, @RequestBody Stock stock) {
        try {
            if(stockPriceService.isStockValid(stock.getTicker())){
                return ResponseEntity.badRequest().body(
                        new ResponseAPI<>(false, "Invalid stock symbol. Please ensure the stock exists in the real world.", null)
                );
            }
            StockOverviewResponse stockOverviewResponse = stockPriceService.getStockDetails(stock.getTicker());
            if (stockOverviewResponse == null || stockOverviewResponse.getName() == null || stockOverviewResponse.getName().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        new ResponseAPI<>(false, "Invalid stock symbol. Please ensure the stock exists in the real world.", null)
                );
            }
            stock.setName(stockOverviewResponse.getName());
            Stock savedStock = stockService.addStock(userId, stock);
            StockDTO stockDTO = DTOMapper.toStockDTO(savedStock);
            ResponseAPI<StockDTO> response = new ResponseAPI<>(true, "Stock added successfully.", stockDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            ResponseAPI<StockDTO> response = new ResponseAPI<>(false, "Error: " + ex.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception ex) {
            ResponseAPI<StockDTO> response = new ResponseAPI<>(false, "An unexpected error occurred: " + ex.getMessage(), null);
            return ResponseEntity.status(500).body(response);
        }
    }


    // Update an existing stock
    @PutMapping("/{id}")
    public ResponseEntity<ResponseAPI<StockDTO>> updateStock(@PathVariable Long id, @RequestBody Stock stock) {
        try {
            // Check if the stock exists

            System.out.println(stock.getName()  );
            Stock existingStock = stockService.getStocksById(id);
            if (existingStock == null) {
                return ResponseEntity.status(404).body(
                        new ResponseAPI<>(false, "Stock with ID " + id + " not found.", null)
                );
            }

            // Update the stock
            stock.setName(existingStock.getName());
            Stock updatedStock = stockService.updateStock(id, stock);

            // Convert to DTO for response
            StockDTO stockDTO = DTOMapper.toStockDTO(updatedStock);
            return ResponseEntity.ok(new ResponseAPI<>(true, "Stock updated successfully.", stockDTO));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new ResponseAPI<>(false, "Error: " + ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ResponseAPI<>(false, "An unexpected error occurred: " + ex.getMessage(), null));
        }
    }

    // Delete a stock
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseAPI<String>> deleteStock(@PathVariable Long id) {
        try {
            stockService.deleteStock(id);
            ResponseAPI<String> response = new ResponseAPI<>(true, "Stock deleted successfully.", null);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            ResponseAPI<String> response = new ResponseAPI<>(false, ex.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception ex) {
            ResponseAPI<String> response = new ResponseAPI<>(false, "An unexpected error occurred: " + ex.getMessage(), null);
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPI<StockDTO>> getStock(@PathVariable Long id) {
        try {
            // Fetch stock from the service layer
            Stock stock = stockService.getStocksById(id);

            // Convert the Stock entity to a DTO
            StockDTO stockDTO = DTOMapper.toStockDTO(stock);

            // Return the DTO in the response
            return ResponseEntity.ok(new ResponseAPI<>(true, "Stock retrieved successfully.", stockDTO));
        } catch (RuntimeException ex) {
            // Handle known runtime exceptions, such as stock not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseAPI<>(false, "Error: " + ex.getMessage(), null));
        } catch (Exception ex) {
            // Handle unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAPI<>(false, "An unexpected error occurred: " + ex.getMessage(), null));
        }
    }

    // Fetch all stocks for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseAPI<List<StockDTO>>> getStocksByUser(@PathVariable Long userId) {
        try {
            List<Stock> stocks = stockService.getStocksByUser(userId);
            List<StockDTO> stockDTOs = stocks.stream()
                    .map(DTOMapper::toStockDTO)
                    .toList();
            ResponseAPI<List<StockDTO>> response = new ResponseAPI<>(true, "Stocks fetched successfully.", stockDTOs);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            ResponseAPI<List<StockDTO>> response = new ResponseAPI<>(false, ex.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception ex) {
            ResponseAPI<List<StockDTO>> response = new ResponseAPI<>(false, "An unexpected error occurred: " + ex.getMessage(), null);
            return ResponseEntity.status(500).body(response);
        }
    }
    @GetMapping("/user/{userId}/{symbol}")
    public ResponseEntity<ResponseAPI<StockDTO>> getStocksByUser(@PathVariable Long userId,@PathVariable String symbol) {
        try {
            List<Stock> stocks = stockService.getStocksByUser(userId);
            Stock stock = stocks.stream()
                    .filter(s -> s.getTicker().equalsIgnoreCase(symbol))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Stock with symbol '" + symbol + "' not found for user ID " + userId));
            StockDTO stockDTO = DTOMapper.toStockDTO(stock);

            // Prepare response
            ResponseAPI<StockDTO> response = new ResponseAPI<>(true, "Stock fetched successfully.", stockDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            ResponseAPI<StockDTO> response = new ResponseAPI<>(false, ex.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception ex) {
            ResponseAPI<StockDTO> response = new ResponseAPI<>(false, "An unexpected error occurred: " + ex.getMessage(), null);
            return ResponseEntity.status(500).body(response);
        }
    }
    @GetMapping("/details/{ticker}")
    public ResponseEntity<ResponseAPI<StockOverviewResponse>> getStockDetailsByTicker(@PathVariable String ticker) {
        try{
            if(stockPriceService.isStockValid(ticker)){
                return ResponseEntity.badRequest().body(
                        new ResponseAPI<>(false, "Invalid stock symbol. Please ensure the stock exists in the real world.", null)
                );
            }
            StockOverviewResponse stockOverviewResponse = stockPriceService.getStockDetails(ticker);
            ResponseAPI<StockOverviewResponse> response = new ResponseAPI<>(true, "Stock details retrieved successfully.", stockOverviewResponse);
            return ResponseEntity.ok(response);
        }catch (RuntimeException ex) {
            ResponseAPI<StockOverviewResponse> response = new ResponseAPI<>(false, ex.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }catch (Exception ex) {
            ResponseAPI<StockOverviewResponse> response = new ResponseAPI<>(false, "An unexpected error occurred.", null);
            return ResponseEntity.status(500).body(response);
        }
    }
    @GetMapping("/{ticker}/intra-day")
    public ResponseEntity<ResponseAPI<IntradayStockDataResponse>> getIntradayStockData(@PathVariable String ticker) {
        try {
            IntradayStockDataResponse intradayStockData = stockPriceService.getIntradayStockData(ticker);
            return ResponseEntity.ok(
                    new ResponseAPI<>(true, "Intraday stock data retrieved successfully.", intradayStockData)
            );
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseAPI<>(false, "Error: " + ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAPI<>(false, "An unexpected error occurred.", null));
        }
    }

}
