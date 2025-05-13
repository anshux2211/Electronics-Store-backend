package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;
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
import java.util.List;

@RestController
@RequestMapping("/product")
//@CrossOrigin("http://localhost:4280")
@Tag(name = "PRODUCT APIs",description = "The ProductControllers class is a Spring Boot REST controller in the com.lcwd.electronic.store.controllers package. It exposes a comprehensive set of APIs to manage products in an electronic store application.")
public class ProductControllers {

    @Autowired
    private ProductService product_service;

    @Autowired
    private FileService file_service;

    @Value("${product.image.path}")
    private String imagePath;


    //create
    @PostMapping("/save")
    @Operation(description = "Creates a new product in the system using the details provided in the request body. It returns the created product with an HTTP 201 status.")
    public ResponseEntity<ProductDto> saveProduct(@Valid @RequestBody ProductDto productDto){
        ProductDto productDto1=product_service.createProduct(productDto);
        return new ResponseEntity<ProductDto>(productDto1, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/update/{productId}")
    @Operation(description = "Updates an existing product identified by productId with the new details provided in the request body. Returns the updated product.")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("productId") String productId,@Valid @RequestBody ProductDto productDto){
        ProductDto productDto1=product_service.updateProduct(productId,productDto);
        return new ResponseEntity<ProductDto>(productDto1,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/delete/{productId}")
    @Operation(description = "Deletes the product identified by productId from the system. Returns a success message upon successful deletion.")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable("productId") String productId){
        product_service.deleteProduct(productId);
        ApiResponseMessage resp=ApiResponseMessage.builder()
                .message("Product Deleted Successfully")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<ApiResponseMessage>(resp,HttpStatus.OK);
    }

    //Get single Product
    @GetMapping("/{productId}")
    @Operation(description = "Retrieves and returns the details of a single product identified by productId.")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable("productId") String productId){
        ProductDto productDto=product_service.getSingleProduct(productId);
        return new ResponseEntity<ProductDto>(productDto,HttpStatus.OK);
    }

    //Get All product
    @GetMapping
    @Operation(description = "Fetches all products in a paginated and sorted format, with optional parameters for page number, page size, sorting field, and sort direction")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(
            @RequestParam(value = "pageNumber",required = false,defaultValue = "0") int pageNumber,
            @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue = "productName") String sortBy,
            @RequestParam(value = "sortDir",required = false,defaultValue = "asc") String sortDir
    ){
        PageableResponse<ProductDto> resp=product_service.getAllProduct(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<PageableResponse<ProductDto>>(resp,HttpStatus.OK);
    }

    //Get All Available Product
    @GetMapping("/stock")
    @Operation(description = "Retrieves all available (in-stock) products in a paginated and sorted manner, similar to the general product listing API.")
    public ResponseEntity<PageableResponse<ProductDto>> getAllAvailableProduct(
            @RequestParam(value = "pageNumber",required = false,defaultValue = "0") int pageNumber,
            @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue = "productName") String sortBy,
            @RequestParam(value = "sortDir",required = false,defaultValue = "asc") String sortDir
    ){
        PageableResponse<ProductDto> resp=product_service.getAvailableProduct(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<PageableResponse<ProductDto>>(resp,HttpStatus.OK);
    }


    // Search Product
    @GetMapping("/search")
    @Operation(description = "Searches for products based on a partial or full name match. Supports pagination and sorting for the result set.")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(
            @RequestParam(value = "name",required = true) String productName,
            @RequestParam(value = "pageNumber",required = false,defaultValue = "0") int pageNumber,
            @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue = "productName") String sortBy,
            @RequestParam(value = "sortDir",required = false,defaultValue = "asc") String sortDir
    ){
        PageableResponse<ProductDto> resp=product_service.searchProduct(productName,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<PageableResponse<ProductDto>>(resp,HttpStatus.OK);
    }

    //Search Brand
    @GetMapping("/brand/{brandName}")
    @Operation(description = "Fetches all products belonging to a specified brand name, with pagination and sorting options.")
    public ResponseEntity<PageableResponse<ProductDto>> searchBrand(
            @PathVariable("brandName") String brandName,
            @RequestParam(value = "pageNumber",required = false,defaultValue = "0") int pageNumber,
            @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue = "productName") String sortBy,
            @RequestParam(value = "sortDir",required = false,defaultValue = "asc") String sortDir
    ){
        PageableResponse<ProductDto> resp=product_service.getProductByBrand(brandName,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<PageableResponse<ProductDto>>(resp,HttpStatus.OK);
    }

    //Get Product By Price Range
    @GetMapping("/range")
    @Operation(description = "Returns products that fall within the specified price range (minPrice to maxPrice) and supports pagination and sorting.")
    public ResponseEntity<PageableResponse<ProductDto>> getProductByRange(
            @RequestParam(value = "minPrice",required = false,defaultValue = "0")int minPrice,
            @RequestParam(value = "maxPrice", required = false,defaultValue = "9999999") int maxPrice,
            @RequestParam(value = "pageNumber",required = false,defaultValue = "0") int pageNumber,
            @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue = "productName") String sortBy,
            @RequestParam(value = "sortDir",required = false,defaultValue = "asc") String sortDir
    ){
        PageableResponse<ProductDto> resp=product_service.getProductByPriceRange(minPrice,maxPrice,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<PageableResponse<ProductDto>>(resp,HttpStatus.OK);
    }

    // Upload Product Image
    @PostMapping("upload/{productId}")
    @Operation(description = "Uploads and associates an image file with the specified product. Updates the product's image name and returns an upload confirmation.")
    public ResponseEntity<ImageResponse> uploadImage(@PathVariable("productId") String productId, @RequestParam("productImage")MultipartFile file) throws IOException {
        ProductDto productDto=product_service.getSingleProduct(productId);
        if(productDto==null){
            ImageResponse resp=ImageResponse.builder()
                    .imageName(null)
                    .message("Product with given ID is not present")
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .build();
            return new ResponseEntity<ImageResponse>(resp,HttpStatus.BAD_REQUEST);
        }

        String imgName=file_service.uploadFile(file,imagePath);
        productDto.setImageName(imgName);
        product_service.updateProduct(productId,productDto);

        ImageResponse resp=ImageResponse.builder()
                .imageName(imgName)
                .message("Image Uploaded Successfully !!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<ImageResponse>(resp,HttpStatus.OK);
    }

    //Download image
    @GetMapping("/download/{productId}")
    @Operation(description = "Downloads the image associated with the product identified by productId. Returns the image file as a response stream.")
    public ResponseEntity<ApiResponseMessage> serveImage(@PathVariable("productId") String productId, HttpServletResponse response) throws IOException {
        ProductDto productDto=product_service.getSingleProduct(productId);
        if(productDto==null){
            ApiResponseMessage resp=ApiResponseMessage.builder()
                    .message("Product with given Id does not exist !!")
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .build();
            return new ResponseEntity<ApiResponseMessage>(resp,HttpStatus.OK);
        }

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        InputStream inputStream=file_service.getResource(imagePath,productDto.getImageName());
        StreamUtils.copy(inputStream,response.getOutputStream());

        ApiResponseMessage resp=ApiResponseMessage.builder()
                .message("Image Downloaded Successfully")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<ApiResponseMessage>(resp,HttpStatus.OK);
    }


    // Api to create Product with Category ID
    @PostMapping("/save/category/{categoryID}")
    @Operation(description = "Creates a new product and directly associates it with a category specified by categoryID.")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable("categoryID") String categoryID,@RequestBody ProductDto productDto){
        ProductDto productDto1=product_service.createProductWithCategory(productDto,categoryID);
        return new ResponseEntity<ProductDto>(productDto1,HttpStatus.OK);
    }

    @PutMapping("/assign/{productId}/category/{categoryId}")
    @Operation(description = "Assigns an existing product to a category using their respective IDs. Returns the updated product.")
    public ResponseEntity<ProductDto> assignProductToCategory(
            @PathVariable("productId") String productId,
            @PathVariable("categoryId") String categoryId
    ){
        ProductDto resp=product_service.assignProductToCategory(productId,categoryId);
        return new ResponseEntity<ProductDto>(resp,HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(description = "Fetches all products associated with a particular category in a paginated format based on categoryId.\n" +
            "\n")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProductCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestParam(value = "pageNumber",required = false,defaultValue = "0") int pageNumber
    ){
        PageableResponse<ProductDto> productDtoList=product_service.getAllProductCategory(categoryId,pageNumber);

        return new ResponseEntity<PageableResponse<ProductDto>>(productDtoList,HttpStatus.OK);
    }


}

