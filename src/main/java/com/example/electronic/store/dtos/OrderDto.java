package com.example.electronic.store.dtos;

import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderDto {

    private String orderId;

    private String orderStatus="PENDING"; //Pending, Dispatched, Delivered
    private String paymentStatus="NOTPAID"; // Pending, Paid
    private int orderAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderDate;
    private Date deliveredDate;

//    private UserDto user;

    private List<OrderItemDto> orderItemList = new ArrayList<>();

}
