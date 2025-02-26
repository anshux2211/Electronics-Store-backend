package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entities.Product;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CategoryDto {
    private String categoryId;

    @Size(min=3,message = "Invalid title !!")
    private String categoryTitle;

    @NotBlank(message = "Description cannot be blank !!")
    private String categoryDetail;

    private String categoryImage;
}
