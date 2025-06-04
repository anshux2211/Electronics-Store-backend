package com.example.electronic.store.controllers;

import com.example.electronic.store.dtos.ApiResponseMessage;
import com.example.electronic.store.dtos.CreateOrderRequest;
import com.example.electronic.store.dtos.OrderDto;
import com.example.electronic.store.dtos.PageableResponse;
import com.example.electronic.store.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "ORDER APIs",description = "The OrderControllers class defines RESTful APIs for managing orders in the electronic store application. It provides endpoints for creating, updating, retrieving, and deleting orders. Role-based access is enforced using Spring Security annotations to ensure that only authorized users can access specific operations.")
public class OrderControllers {

    @Autowired
    private OrderService order_service;

    @PostMapping("/create")
    @Operation(description = "This endpoint allows a user to create a new order. It accepts a CreateOrderRequest object in the request body, which contains all the necessary details to place an order. Upon successful creation, the API returns the newly created OrderDto along with HTTP status 201 Created.")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request){
        OrderDto resp=order_service.createOrder(request);
        return new ResponseEntity<OrderDto>(resp, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{orderId}")
    @Operation(description = "This API enables users with NORMAL or ADMIN roles to delete an existing order by providing the orderId in the path variable. On successful deletion, a confirmation message with HTTP status 200 OK is returned.")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable("orderId") String orderId){
        order_service.removeOrder(orderId);
        ApiResponseMessage resp=ApiResponseMessage.builder()
                .message("Order Deleted Successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<ApiResponseMessage>(resp,HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    @Operation(description = "This endpoint allows users with NORMAL or ADMIN roles to retrieve all orders placed by a specific user. The userId is passed as a path variable, and a list of OrderDto objects is returned upon success.")
    public ResponseEntity<List<OrderDto>> GetOrderOfUser(@PathVariable("userId") String userId){
        List<OrderDto> resp=order_service.getOrdersofUser(userId);
        return new ResponseEntity<List<OrderDto>>(resp,HttpStatus.OK);

    }

    @GetMapping
    @Operation(description = "This API fetches a paginated list of all orders in the system, accessible to ADMIN. It supports pagination and sorting through optional query parameters: pageNumber, pageSize, sortBy, and sortDir, and returns a PageableResponse of OrderDto.")
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
            @RequestParam(value = "pageNumber",required = false,defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue = "orderId") String sortBy,
            @RequestParam(value = "sortDir",required = false,defaultValue = "asc") String sortDir
    ){
        PageableResponse<OrderDto> resp=order_service.getOrders(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<PageableResponse<OrderDto>>(resp,HttpStatus.OK);
    }


    @PutMapping("/update/{orderId}")
    @Operation(description = "This endpoint, restricted to admin users, allows updating the status of an existing order. It accepts optional query parameters for orderStatus, paymentStatus, and deliveredDate (in yyyy-MM-dd format), and updates the corresponding fields of the order identified by orderId. The updated OrderDto is returned with HTTP status 200 OK.")
    public ResponseEntity<OrderDto> updateOrder(
        @RequestParam(value = "orderStatus",required = false,defaultValue = "null") String orderStatus,
        @RequestParam(value = "paymentStatus",required = false,defaultValue = "null") String paymentStatus,
        @RequestParam(value = "deliveredDate",required = false,defaultValue = "null") String deliveredDate,
        @PathVariable("orderId") String orderId
        ){
        Date parsedDate = null;

        if (deliveredDate != null && !deliveredDate.equalsIgnoreCase("null")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                parsedDate = sdf.parse(deliveredDate);
            } catch (Exception e) {
                throw new RuntimeException("Invalid date format! Please use yyyy-MM-dd");
            }
        }

        OrderDto resp=order_service.updateOrder(orderId,orderStatus,paymentStatus,parsedDate);
        return new ResponseEntity<OrderDto>(resp,HttpStatus.OK);
    }

}
