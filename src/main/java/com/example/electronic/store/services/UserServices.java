package com.example.electronic.store.services;

import com.example.electronic.store.dtos.JwtRequest;
import com.example.electronic.store.dtos.PageableResponse;
import com.example.electronic.store.dtos.UserDto;

public interface UserServices {
    //create
    UserDto createUser(UserDto user_dto);

    //update
    UserDto updateUser(UserDto user_dto, String userId);

    Boolean change_password(JwtRequest jwtRequest);

    //delete
    void deleteUser(String userId);

    //get all users
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single user by ID
    UserDto getSingleUser(String userId);

    // get single user by email
    UserDto getUserByMail(String userMail);

    // get single user by phone number
    UserDto getUserByPhone(String userPhone);

    PageableResponse<UserDto> searchUser(String name,int pageNumber,int pageSize, String sortBy, String sortDir);

    // Add image Name
    void addImageName(String userId,String imageName);


}
