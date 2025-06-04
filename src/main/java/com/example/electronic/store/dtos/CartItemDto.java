package com.example.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
    private int cartItemId;
    private ProductDto product;
    private int quantity;
    private int totalPrice;
}
