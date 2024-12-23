package org.example.portfoliomanager.controller;

import org.example.portfoliomanager.dto.DTOMapper;
import org.example.portfoliomanager.dto.ResponseAPI;
import org.example.portfoliomanager.dto.StockDTO;
import org.example.portfoliomanager.models.Stock;
import org.example.portfoliomanager.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // Add a new stock
    @PostMapping("/{userId}")
    public ResponseEntity<ResponseAPI<StockDTO>> addStock(@PathVariable Long userId, @RequestBody Stock stock) {
        try {
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
            Stock existingStock = stockService.getStocksById(id);
            if (existingStock == null) {
                return ResponseEntity.status(404).body(
                        new ResponseAPI<>(false, "Stock with ID " + id + " not found.", null)
                );
            }

            // Update the stock
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


}
