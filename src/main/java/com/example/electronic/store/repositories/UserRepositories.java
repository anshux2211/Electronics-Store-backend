package com.example.electronic.store.repositories;

import com.example.electronic.store.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositories extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Page<User> findByNameContaining(String name, Pageable pageable);
}
