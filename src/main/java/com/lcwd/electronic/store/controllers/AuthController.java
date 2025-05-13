package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.JwtRequest;
import com.lcwd.electronic.store.dtos.JwtResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.security.JwtHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "AUTH APIs",description = "This controller is responsible for handling user authentication in the Electronic Store application. It verifies user credentials and generates a secure JWT token for successfully authenticated users. This token is used for authorizing subsequent API requests across the application.\n" +
        "\n")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwthelper;

    @Autowired
    private ModelMapper mapper;

    Logger logger= LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/generate-token")
    @Operation(description = "Get JWT token by calling this API. You need to enter your registered username and password before.")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
        logger.info("Username:{},Password: {}",request.getUsername(),request.getPassword());

        this.doAuthenticate(request.getUsername(),request.getPassword());
        User user= (User) userDetailsService.loadUserByUsername(request.getUsername());
        String token=jwthelper.generateToken(user);

        JwtResponse resp=JwtResponse.builder()
                .token(token)
                .user(mapper.map(user, UserDto.class))
                .build();
        return ResponseEntity.ok(resp);
    }

    private void doAuthenticate(String username, String password) {
        try{
            Authentication authentication=new UsernamePasswordAuthenticationToken(username,password);
            authenticationManager.authenticate(authentication);
        }
        catch(BadCredentialsException ex){
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }
}
