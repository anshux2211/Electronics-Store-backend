package com.example.electronic.store.services;

import com.example.electronic.store.dtos.PageableResponse;
import com.example.electronic.store.dtos.ProductDto;

public interface ProductService {
    //create
    public ProductDto createProduct(ProductDto productDto);

    //update
    public ProductDto updateProduct(String productId, ProductDto productDto);

    //delete
    public void deleteProduct(String productId);

    //get single
    public ProductDto getSingleProduct(String productId);

    //get all
    public PageableResponse<ProductDto> getAllProduct(int pageNumber,int pageSize,String sortBy, String sortDir);

    //get all availability
    public PageableResponse<ProductDto> getAvailableProduct(int pageNumber,int pageSize,String sortBy, String sortDir);

    //search product
    public PageableResponse<ProductDto> searchProduct(String name,int pageNumber,int pageSize,String sortBy, String sortDir);

    // get product by brand
    public PageableResponse<ProductDto> getProductByBrand(String brand, int pageNumber,int pageSize,String sortBy, String sortDir);

    //get product whose price is between l and r
    public PageableResponse<ProductDto> getProductByPriceRange(int low,int high, int pageNumber,int pageSize,String sortBy, String sortDir);

    // Create Product with category
    public ProductDto createProductWithCategory(ProductDto productDto,String categoryId);

    // Assign Product to a Category
    public ProductDto assignProductToCategory(String productId,String categoryId);

    // Fetch All products under particular category
    public PageableResponse<ProductDto> getAllProductCategory(String categoryId,int pageNumber);
}
