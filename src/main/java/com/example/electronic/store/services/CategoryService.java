package com.example.electronic.store.services;

import com.example.electronic.store.dtos.CategoryDto;
import com.example.electronic.store.dtos.PageableResponse;

public interface CategoryService {
    //create
    public CategoryDto saveCategory(CategoryDto categoryDto);

    //update
    public CategoryDto updateCategory(String categoryId,CategoryDto categoryDto);

    //delete
    public void deleteCategory(String categoryId);

    //get all category
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single category
    public CategoryDto getCategory(String categoryId);

}
