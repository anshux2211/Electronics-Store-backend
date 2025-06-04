package com.example.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class Order {

    @Id
    private String orderId;

    private String orderStatus; //Pending, Dispatched, Delivered
    private String paymentStatus; // Pending, Paid
    private int orderAmount;
    @Column(length = 100)
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderDate;
    private Date deliveredDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> orderItemList = new ArrayList<>();


}
