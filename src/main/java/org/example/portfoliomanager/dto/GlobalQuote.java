package org.example.portfoliomanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlobalQuote {
    @JsonProperty("05. price")
    private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
