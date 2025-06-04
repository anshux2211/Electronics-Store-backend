package com.example.electronic.store.dtos;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CartDto {
    private String cartId;
    private Date createdAt;
    private UserDto user;
    private List<CartItemDto> cartItemList;
}
