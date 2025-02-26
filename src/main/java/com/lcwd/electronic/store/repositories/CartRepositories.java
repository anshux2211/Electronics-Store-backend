package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepositories extends JpaRepository<Cart,String> {
    Optional<Cart> findByUser(User user);
}
