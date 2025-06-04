package com.example.electronic.store.repositories;

import com.example.electronic.store.entities.Category;
import com.example.electronic.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepositories extends JpaRepository<Product,String> {
    Page<Product> findByAvailabilityTrue(Pageable pageable);
    Page<Product> findByProductNameContaining(String name,Pageable pageable);
    Page<Product> findByDiscountedPriceBetween(int minPrice,int maxPrice,Pageable pageable);
    Page<Product> findByBrand(String name,Pageable pageable);
    Page<Product> findByCategory(Category category, Pageable pageable);
}
