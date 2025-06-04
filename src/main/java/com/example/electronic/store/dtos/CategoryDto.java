package com.example.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

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
