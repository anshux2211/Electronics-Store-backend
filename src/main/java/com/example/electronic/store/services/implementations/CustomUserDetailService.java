package com.example.electronic.store.services.implementations;

import com.example.electronic.store.entities.User;
import com.example.electronic.store.exception.ResourceNotFoundException;
import com.example.electronic.store.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepositories userRepositories;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepositories.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User with given usedId doesn't exist !!"));
        return user;
    }
}
