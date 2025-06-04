package com.example.electronic.store.services;

import com.example.electronic.store.dtos.CartDto;

public interface CartService {

    //save items
    // Case 1: If cart is not created: we create a cart and then add item
    // Case 2: If cart is present, we just add item
    public CartDto addItem(String userId,String productId,int quantity);

    // Remove Item from a cart;
    public void removeItem(String userId,int cartItemId);

    // Clear cart
    public void clearCart(String userId);

    //Fetch cart
    public CartDto fetchCart(String userId);
}
