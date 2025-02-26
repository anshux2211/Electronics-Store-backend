package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserServices {
    //create
    UserDto createUser(UserDto user_dto);

    //update
    UserDto updateUser(UserDto user_dto, String userId);

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

    // other user specific operations



}
