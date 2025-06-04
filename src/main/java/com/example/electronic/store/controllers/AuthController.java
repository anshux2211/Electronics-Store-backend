package com.example.electronic.store.controllers;

import com.example.electronic.store.dtos.ApiResponseMessage;
import com.example.electronic.store.dtos.JwtRequest;
import com.example.electronic.store.dtos.JwtResponse;
import com.example.electronic.store.dtos.UserDto;
import com.example.electronic.store.entities.User;
import com.example.electronic.store.security.JwtHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Operation(description = "This is a public API. Get JWT token by calling this API. You need to enter your registered username and password before.")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){

        this.doAuthenticate(request.getUsername(),request.getPassword());
        User user= (User) userDetailsService.loadUserByUsername(request.getUsername());
        String token=jwthelper.generateToken(user);

        JwtResponse resp=JwtResponse.builder()
                .token(token)
                .user(mapper.map(user, UserDto.class))
                .build();
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/verify")
    @Operation(description = "This API is accessible registered users and ADMINS. Takes username and password in JwtRequest and verifies the credentials. Returns a JSON response containing a message, status code, and success flag. ")
    public ResponseEntity<ApiResponseMessage> verifyPassword(@RequestBody JwtRequest request){
        this.doAuthenticate(request.getUsername(),request.getPassword());
        ApiResponseMessage resp=ApiResponseMessage.builder()
                .message("Successfull")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<ApiResponseMessage>(resp,HttpStatus.OK);
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
