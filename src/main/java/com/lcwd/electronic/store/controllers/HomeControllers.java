package com.lcwd.electronic.store.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class HomeControllers {
    @GetMapping
    public String testing(){
        return "Welcome to Electronics Store";
    }
}
