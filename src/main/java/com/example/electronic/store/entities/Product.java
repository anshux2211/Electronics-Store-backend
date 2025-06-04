package com.example.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "product")
public class Product {
    @Id
    private String productId;
    private String productName;
    @Column(length = 10000)
    private String productDetail;
    private int originalPrice;
    private int discountedPrice;
    private int quantity;
    private Date addeddate;
    private boolean availability;
    private String brand;
    private String imageName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

}
