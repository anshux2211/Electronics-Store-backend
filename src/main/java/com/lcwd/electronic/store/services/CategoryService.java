package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;

import java.util.List;

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
