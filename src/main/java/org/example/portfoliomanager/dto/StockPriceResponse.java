package org.example.portfoliomanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockPriceResponse {
    @JsonProperty("Global Quote")
    private GlobalQuote globalQuote;

    public GlobalQuote getGlobalQuote() {
        return globalQuote;
    }

    public void setGlobalQuote(GlobalQuote globalQuote) {
        this.globalQuote = globalQuote;
    }
}