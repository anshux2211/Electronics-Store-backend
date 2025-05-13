package com.lcwd.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {
    @NotBlank(message = "CartId can't be Blank !!")
    private String cartId;
    @NotBlank(message = "userId can't be Blank !!")
    private String userId;


    private String orderStatus="PENDING"; //Pending, Dispatched, Delivered
    private String paymentStatus="NOTPAID"; // Pending, Paid

    @NotBlank(message = "Address is required !!")
    private String billingAddress;

    @NotBlank(message = "Phone Number is required !!")
    private String billingPhone;

    @NotBlank(message = "Billing Name is required !!")
    private String billingName;
}
