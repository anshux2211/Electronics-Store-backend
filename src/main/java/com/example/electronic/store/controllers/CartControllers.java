package com.example.electronic.store.controllers;

import com.example.electronic.store.dtos.ApiResponseMessage;
import com.example.electronic.store.dtos.CartDto;
import com.example.electronic.store.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@Tag(name = "CART APIs",description = "This controller manages all operations related to the shopping cart in the Electronic Store application. It allows users to add products to their cart, remove specific items, clear the entire cart, and fetch the current cart details. Each operation is associated with a specific user identified by their userId")
public class CartControllers {
    @Autowired
    private CartService cart_service;

    @PostMapping("/save/{userId}")
    @Operation(description = "This API is accessible to registered users only. This API adds a specified product to a user's cart. The client must provide the user ID as a path variable, along with the product ID and quantity as request parameters. Upon successful addition, the updated cart details are returned.")
    public ResponseEntity<CartDto> saveItem(
            @PathVariable("userId") String userId,
            @RequestParam(value = "productId",required = true) String productId,
            @RequestParam(value = "quantity",required = true) int quantity
    ){
        CartDto cartDto=cart_service.addItem(userId,productId,quantity);
        return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
    }

    @PutMapping("/remove/{userId}/{cartItemId}")
    @Operation(description = "This API is accessible to registered users only. This endpoint allows the removal of a specific item from a user's cart. It requires the user ID and the cart item ID as path variables. After successfully removing the item, a confirmation message is returned.\n" +
            "\n")
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
    @Operation(description = "This API is accessible to registered users only. This API clears all items from a user's cart. It takes the user ID as a path variable and returns a success message once the cart has been emptied.")
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
    @Operation(description = "This API is accessible to registered users only. This endpoint retrieves the current contents of a user's cart. The user ID is passed as a path variable, and the API returns the full cart details including all items and their quantities.")
    public ResponseEntity<CartDto> getCart(@PathVariable("userId") String userId){
        CartDto cartDto=cart_service.fetchCart(userId);
        return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
    }


}
