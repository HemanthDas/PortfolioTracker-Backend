package org.example.portfoliomanager.service;

import org.example.portfoliomanager.dto.IntradayStockDataResponse;
import org.example.portfoliomanager.dto.StockOverviewResponse;
import org.example.portfoliomanager.dto.StockPriceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockPriceService {
    private static final String API_URL = "https://www.alphavantage.co/query";

    @Value("${APIKEY}")
    private String API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Retrieves the latest stock price for a given ticker.
     */
    @Cacheable("stockPrices")
    public Double getStockPrice(String ticker) {
        String url = String.format("%s?function=GLOBAL_QUOTE&symbol=%s&apikey=%s", API_URL, ticker, API_KEY);
        try {
            StockPriceResponse response = restTemplate.getForObject(url, StockPriceResponse.class);
            if (response != null && response.getGlobalQuote() != null) {
                return Double.valueOf(response.getGlobalQuote().getPrice());
            }
        } catch (Exception e) {
            System.err.println("Error fetching stock price: " + e.getMessage());
        }
        return 0.0;
    }

    /**
     * Validates the existence of a stock by checking its overview data.
     */
    @Cacheable("stockValidation")
    public boolean isStockValid(String ticker) {
        String url = String.format("%s?function=OVERVIEW&symbol=%s&apikey=%s", API_URL, ticker, API_KEY);
        try {
            StockOverviewResponse response = restTemplate.getForObject(url, StockOverviewResponse.class);
            return response == null || response.getName() == null || response.getName().isEmpty();
        } catch (Exception e) {
            System.err.println("Error validating stock ticker [" + ticker + "]: " + e.getMessage());
        }
        return true;
    }

    /**
     * Fetches detailed information about a stock.
     */
    @Cacheable("stockDetails")
    public StockOverviewResponse getStockDetails(String ticker) {
        String url = String.format("%s?function=OVERVIEW&symbol=%s&apikey=%s", API_URL, ticker, API_KEY);
        try {
            StockOverviewResponse response = restTemplate.getForObject(url, StockOverviewResponse.class);
            if (response == null || response.getName() == null || response.getName().isEmpty()) {
                throw new RuntimeException("No data found for stock symbol: " + ticker);
            }
            return response;
        } catch (Exception e) {
            System.err.println("Error fetching stock details for [" + ticker + "]: " + e.getMessage());
            throw new RuntimeException("Error fetching stock details for [" + ticker + "]: " + e.getMessage());
        }
    }

    /**
     * Fetches intraday stock data for a given ticker and prepares it for visualization.
     */
    @Cacheable("intradayStockData")
    public IntradayStockDataResponse getIntradayStockData(String ticker) {
        String url = String.format("%s?function=TIME_SERIES_INTRADAY&symbol=%s&interval=5min&apikey=%s", API_URL, ticker, API_KEY);
        try {
            return restTemplate.getForObject(url, IntradayStockDataResponse.class);
        } catch (Exception e) {
            System.err.println("Error fetching intraday stock data: " + e.getMessage());
            throw new RuntimeException("Unable to fetch intraday stock data for ticker: " + ticker);
        }
    }

}