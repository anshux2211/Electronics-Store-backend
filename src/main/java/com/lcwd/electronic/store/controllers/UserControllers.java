package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "USER APIs",description = "This controller provides RESTful endpoints for managing user-related operations in the Electronic Store application. It allows clients to perform CRUD operations on user data, search users, and manage user profile images. The APIs are well-structured for frontend integration and use DTOs for clean data exchange.")
public class UserControllers {

    @Autowired
    private UserServices user_service;

    @Autowired
    private FileService file_service;

    @Value("${user.profile.image.path}")
    private String imagePath;

    //create
    @PostMapping("/save")
    @Operation(description = "Creates a new user by accepting user details in the request body. Validates the input and returns the created user along with HTTP 201 status.")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user){
        UserDto user1= user_service.createUser(user);
        if(user1==null)
            return new ResponseEntity<>(user1, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(user1,HttpStatus.CREATED);
    }

    @PutMapping("/update/{userId}")
    @Operation(description = "Updates the details of an existing user identified by userId. Accepts new data in the request body and returns the updated user.")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto user, @PathVariable String userId){
        UserDto user1=user_service.updateUser(user,userId);

        if(user1==null)
            return new ResponseEntity<>(user1, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(user1,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    @Operation(description = "Deletes the user specified by userId from the system and returns a success message upon successful removal.")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId){
        user_service.deleteUser(userId);
        ApiResponseMessage resp= ApiResponseMessage.builder().message("User deleted Successfully").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @GetMapping
    @Operation(description = "Retrieves all users with pagination and sorting support. Accepts optional query parameters for page number, page size, sort field, and sort direction.")
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber",required = false,defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue = "name") String sortBy,
            @RequestParam(value = "sortDir", required = false,defaultValue = "ASC") String sortDir
    ){
        System.out.println(pageNumber+" "+pageSize+" hey");
        PageableResponse<UserDto> userList=user_service.getAllUser(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(userList,HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @Operation(description = "Fetches and returns the user details for a specific user identified by userId.")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable String userId){
        UserDto user1=user_service.getSingleUser(userId);

        if(user1==null)
            return new ResponseEntity<>(user1, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(user1,HttpStatus.OK);
    }

    @GetMapping("/mail/{email}")
    @Operation(description = "Finds and returns user information based on the provided email address.")
    public ResponseEntity<UserDto> getUserByMail(@PathVariable String email){
        UserDto user1=user_service.getUserByMail(email);

        if(user1==null)
            return new ResponseEntity<>(user1, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(user1,HttpStatus.OK);
    }

    @GetMapping("/phone/{phoneNo}")
    @Operation(description = "Retrieves a user’s details using their phone number as the identifier")
    public ResponseEntity<UserDto> getUserByPhone(@PathVariable String phoneNo){
        UserDto user1=user_service.getUserByPhone(phoneNo);

        if(user1==null)
            return new ResponseEntity<>(user1, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(user1,HttpStatus.OK);
    }

    @GetMapping("/search/{name}")
    @Operation(description = "Searches for users whose names contain the provided search keyword. Supports pagination and sorting of results.")
    public ResponseEntity<PageableResponse<UserDto>> searchUsers(
            @PathVariable String name,
            @RequestParam(value = "pageNumber",required = false,defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue = "name") String sortBy,
            @RequestParam(value = "sortDir", required = false,defaultValue = "ASC") String sortDir
            ){

        PageableResponse<UserDto> userList=user_service.searchUser(name,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(userList,HttpStatus.OK);
    }

    // Upload User Image
    @PostMapping("/image/{userID}")
    @Operation(description = "Uploads a profile image for the user identified by userID. Stores the image and updates the user record with the image name.")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam("userImage") MultipartFile file,
                                                     @PathVariable("userID") String userId) throws IOException {

        System.out.println("UserId: "+userId);

        UserDto user=user_service.getSingleUser(userId);
        if(user==null){
            ImageResponse imgResp=ImageResponse.builder()
                    .message("User With given Id is not present")
                    .imageName(null)
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .build();

            return new ResponseEntity<ImageResponse>(imgResp,HttpStatus.BAD_REQUEST);
        }

        String imageName=file_service.uploadFile(file,imagePath);
        user_service.addImageName(userId,imageName);

        ImageResponse imgResp=ImageResponse.builder()
                .message("Image Uploaded Successfully")
                .imageName(imageName)
                .status(HttpStatus.ACCEPTED)
                .success(true)
                .build();

        return new ResponseEntity<ImageResponse>(imgResp,HttpStatus.OK);
    }

    // Serve User Image
    @GetMapping("/image/{userID}")
    @Operation(description = "Serves the profile image of the user identified by userID directly in the HTTP response stream, and also returns a status message.")
    public ResponseEntity<ImageResponse> serveImage(@PathVariable ("userID") String userId, HttpServletResponse response) throws IOException {
        UserDto user=user_service.getSingleUser(userId);
        String image_name=user.getImageName();

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        InputStream inputStream=file_service.getResource(imagePath,image_name);
        StreamUtils.copy(inputStream,response.getOutputStream());

        ImageResponse resp=ImageResponse.builder()
                .message("Successfully received !!")
                .imageName(image_name)
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<ImageResponse>(resp,HttpStatus.OK);
    }

}
