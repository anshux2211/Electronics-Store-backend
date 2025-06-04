package com.example.electronic.store.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class HomeControllers {

    @GetMapping
    @Operation(description = "This API is just for testing")
    public String testing(){
        return "Welcome to Electronics Store";
    }

}
