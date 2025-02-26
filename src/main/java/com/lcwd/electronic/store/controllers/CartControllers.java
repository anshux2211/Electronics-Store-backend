package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.dtos.CartItemDto;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.services.CartService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartControllers {
    @Autowired
    private CartService cart_service;

    @PostMapping("/save/{userId}")
    public ResponseEntity<CartDto> saveItem(
            @PathVariable("userId") String userId,
            @RequestParam(value = "productId",required = true) String productId,
            @RequestParam(value = "quantity",required = true) int quantity
    ){
        CartDto cartDto=cart_service.addItem(userId,productId,quantity);
        return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
    }

    @PutMapping("/remove/{userId}/{cartItemId}")
    public ResponseEntity<ApiResponseMessage> removeItem(
            @PathVariable("userId") String userId,
            @PathVariable("cartItemId") int cartItemId
    ){
        cart_service.removeItem(userId,cartItemId);
        ApiResponseMessage resp=ApiResponseMessage.builder()
                .message("Item Removed Successfully !!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<ApiResponseMessage>(resp,HttpStatus.OK);
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(
            @PathVariable("userId") String userId
    ){
        cart_service.clearCart(userId);
        ApiResponseMessage resp=ApiResponseMessage.builder()
                .message("Cart cleared Successfully !!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<ApiResponseMessage>(resp,HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable("userId") String userId){
        CartDto cartDto=cart_service.fetchCart(userId);
        return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
    }


}
