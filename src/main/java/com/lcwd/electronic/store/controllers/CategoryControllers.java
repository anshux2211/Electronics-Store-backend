package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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
import java.util.logging.Handler;

@RestController
@RequestMapping("/category")
public class CategoryControllers {

    @Autowired
    private CategoryService category_service;

    @Autowired
    private FileService file_service;

    @Value("${category.image.path}")
    private String imagePath;

    //save
    @PostMapping("/save")
    public ResponseEntity<CategoryDto> saveCategory(@Valid @RequestBody CategoryDto categorydto){
        CategoryDto savedCategory=category_service.saveCategory(categorydto);
        return new ResponseEntity<CategoryDto>(savedCategory, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable("categoryId") String categoryId){
        CategoryDto updatedCategory=category_service.updateCategory(categoryId,categoryDto);
        return new ResponseEntity<CategoryDto>(updatedCategory,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/delete/{categoryId}")
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
    public ResponseEntity<CategoryDto> getSingleCatrgory(@PathVariable("categoryId") String categoryId){
        CategoryDto category=category_service.getCategory(categoryId);
        return new ResponseEntity<CategoryDto>(category,HttpStatus.OK);
    }


    //save image
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam("categoryImage") MultipartFile file,@PathVariable(value = "categoryId",required = true) String categoryId) throws IOException {

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
    public void serveImage(
            @PathVariable(value = "categoryId",required = true) String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto categoryDto=category_service.getCategory(categoryId);
        InputStream inputStream=file_service.getResource(imagePath,categoryDto.getCategoryImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream,response.getOutputStream());
        return;
    }

}
