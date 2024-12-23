package org.example.portfoliomanager.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.annotation.JsonProperty;


@Service
public class StockPriceService {
    private static final String API_URL = "https://www.alphavantage.co/query";
    private static final String API_KEY = "G1RA5M9PIX6H4UUI";

    @Cacheable("stockPrices")
    public Double getStockPrice(String ticker) {
        String url = String.format("%s?function=GLOBAL_QUOTE&symbol=%s&outputsize=compact&apikey=%s", API_URL, ticker, API_KEY);
        RestTemplate restTemplate = new RestTemplate();
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
}

class StockPriceResponse {
    @JsonProperty("Global Quote")
    private GlobalQuote globalQuote;

    public GlobalQuote getGlobalQuote() {
        return globalQuote;
    }

    public void setGlobalQuote(GlobalQuote globalQuote) {
        this.globalQuote = globalQuote;
    }
}

class GlobalQuote {
    @JsonProperty("05. price")
    private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
