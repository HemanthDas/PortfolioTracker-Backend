package org.example.portfoliomanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PortfolioManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioManagerApplication.class, args);
    }

}
