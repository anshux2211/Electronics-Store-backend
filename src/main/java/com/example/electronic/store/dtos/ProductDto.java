package com.example.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDto {
    private String productId;

    @Size(min=3,message = "Product Name must be greater than 2 !!")
    private String productName;

    @Size(min=3,max = 10000, message = "Invalid product detail !!")
    private String productDetail;

    @NotNull(message = "Original Price cannot be empty !!")
    private int originalPrice;

    @NotNull(message = "Discounted Price cannot be empty !!")
    private int discountedPrice;

    @NotNull(message = "Quantity cannot be blank !!")
    private int quantity;

    private Date addeddate;

    @NotNull(message = "Availability cannot be blank !!")
    private boolean availability;

    @NotBlank(message = "Brand cannot be blank !!")
    private String brand;

    private String imageName;

    private CategoryDto category;
}
