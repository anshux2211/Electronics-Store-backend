package com.example.electronic.store.repositories;

import com.example.electronic.store.entities.Cart;
import com.example.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepositories extends JpaRepository<Cart,String> {
    Optional<Cart> findByUser(User user);
}
