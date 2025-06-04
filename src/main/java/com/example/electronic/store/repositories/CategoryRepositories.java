package com.example.electronic.store.repositories;

import com.example.electronic.store.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepositories extends JpaRepository<Category,String> {
}
