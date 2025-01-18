package org.example.portfoliomanager.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/greet")
public class GreetingsController {
    @GetMapping("")
    public String greet() {
        return "Hello World, API is Running Successfully!";
    }
}
