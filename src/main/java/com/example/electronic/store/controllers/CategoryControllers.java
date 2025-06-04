package com.example.electronic.store.controllers;

import com.example.electronic.store.dtos.ApiResponseMessage;
import com.example.electronic.store.dtos.CategoryDto;
import com.example.electronic.store.dtos.ImageResponse;
import com.example.electronic.store.dtos.PageableResponse;
import com.example.electronic.store.services.CategoryService;
import com.example.electronic.store.services.FileService;
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

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/category")
@Tag(name = "CATEGORY APIs",description = "The CategoryControllers class is responsible for handling all category-related operations in the Electronic Store application. It provides RESTful endpoints to create, update, delete, retrieve, and manage images for product categories. This controller acts as a bridge between the client requests and the service layer (CategoryService and FileService).")
public class CategoryControllers {

    @Autowired
    private CategoryService category_service;

    @Autowired
    private FileService file_service;

    @Value("${category.image.path}")
    private String imagePath;

    //save
    @PostMapping("/save")
    @Operation(description = "This API is accessible to ADMIN only. This API creates a new category in the system. It accepts a CategoryDto object in the request body, validates it, and then delegates the creation task to the service layer. Upon successful creation, the newly created category object is returned with HTTP status 201 Created.\n" +
            "\n")
    public ResponseEntity<CategoryDto> saveCategory(@Valid @RequestBody CategoryDto categorydto){
        CategoryDto savedCategory=category_service.saveCategory(categorydto);
        return new ResponseEntity<CategoryDto>(savedCategory, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/update/{categoryId}")
    @Operation(description = "This API is accessible to ADMIN only. This endpoint updates the details of an existing category. It takes the category ID as a path variable and the updated CategoryDto as the request body. On successful update, it returns the updated category data with HTTP status 200 OK.")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable("categoryId") String categoryId){
        CategoryDto updatedCategory=category_service.updateCategory(categoryId,categoryDto);
        return new ResponseEntity<CategoryDto>(updatedCategory,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/delete/{categoryId}")
    @Operation(description = "This API is accessible to ADMIN only. This API deletes a category by its ID. The category ID is passed as a path variable. If the deletion is successful, it responds with a confirmation message and HTTP status 200 OK.")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable("categoryId") String categoryId){
        category_service.deleteCategory(categoryId);
        ApiResponseMessage resp=ApiResponseMessage.builder()
                .message("Category Deleted Successfully")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<ApiResponseMessage>(resp,HttpStatus.OK);
    }

    //get all
    @GetMapping
    @Operation(description = "This is a public API. This endpoint retrieves a paginated list of all categories. It supports optional query parameters like pageNumber, pageSize, sortBy, and sortDir to control pagination and sorting. It returns a paginated response with category details.")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(
            @RequestParam(value = "pageNumber",required = false,defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue = "categoryTitle") String sortBy,
            @RequestParam(value = "sortDir",required = false,defaultValue = "ASC") String sortDir
    ){
        PageableResponse<CategoryDto> resp=category_service.getAllCategory(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<PageableResponse<CategoryDto>>(resp,HttpStatus.OK);
    }

    //get single
    @GetMapping("/{categoryId}")
    @Operation(description = "This is a public API. This API fetches a single category by its ID. The category ID is provided as a path variable. If found, it returns the category’s details with HTTP status 200 OK.")
    public ResponseEntity<CategoryDto> getSingleCatrgory(@PathVariable("categoryId") String categoryId){
        CategoryDto category=category_service.getCategory(categoryId);
        return new ResponseEntity<CategoryDto>(category,HttpStatus.OK);
    }


    //save image
    @PostMapping("/image/{categoryId}")
    @Operation(description = "This API is accessible to ADMIN only. This endpoint uploads an image for a specific category. It accepts the image file as a multipart form data and the category ID as a path variable. If the category exists, the image is uploaded and linked to the category, and a success response with image details is returned.")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam("categoryImage") MultipartFile file, @PathVariable(value = "categoryId",required = true) String categoryId) throws IOException {

        if(category_service.getCategory(categoryId)==null){
            ImageResponse resp=ImageResponse.builder()
                    .imageName(null)
                    .message("Category with given Id is not present!!")
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .build();
            return new ResponseEntity<ImageResponse>(resp,HttpStatus.BAD_REQUEST);
        }

        String fullName=file_service.uploadFile(file,imagePath);
        CategoryDto categoryDto=category_service.getCategory(categoryId);
        categoryDto.setCategoryImage(fullName);
        CategoryDto category=category_service.saveCategory(categoryDto);
        ImageResponse resp=ImageResponse.builder()
                .imageName(fullName)
                .message("Image Uploaded Successfully")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<ImageResponse>(resp,HttpStatus.OK);
    }


    //serve image
    @GetMapping("/download/{categoryId}")
    @Operation(description = "This is a public API. This API serves the image associated with a given category ID. It retrieves the image from the file system and streams it back in the HTTP response with a Content-Type of image/jpeg.")
    public void serveImage(
            @PathVariable(value = "categoryId",required = true) String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto categoryDto=category_service.getCategory(categoryId);
        InputStream inputStream=file_service.getResource(imagePath,categoryDto.getCategoryImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream,response.getOutputStream());
        return;
    }

}
