package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;

import java.util.Date;
import java.util.List;

public interface OrderService {
    // Create Order
    OrderDto createOrder(CreateOrderRequest orderDto);

    // remove User
    void removeOrder(String orderId);

    // get Orders of users
    List<OrderDto> getOrdersofUser(String userId);

    // Get All orders
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

    // Update Order
    OrderDto updateOrder(String orderId, String orderStatus, String paymentStatus, Date delivereddate);

}
