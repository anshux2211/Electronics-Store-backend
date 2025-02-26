package com.lcwd.electronic.store.services.implementations;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.ToPageableResponse;
import com.lcwd.electronic.store.repositories.CategoryRepositories;
import com.lcwd.electronic.store.repositories.ProductRepositories;
import com.lcwd.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepositories product_repo;
    @Autowired
    private CategoryRepositories category_repo;
    @Autowired
    private ModelMapper mapper;

    @Value("${product.image.path}")
    private String imagePath;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        if(productDto.getProductId()==null){
            String Id= UUID.randomUUID().toString();
            productDto.setProductId(Id);
        }
        Date currDate=new Date();
        productDto.setAddeddate(currDate);
        Product product=product_repo.save(mapper.map(productDto,Product.class));
        return mapper.map(product,ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(String productId, ProductDto productDto) {
        Product product=product_repo.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product with given Id is not present"));

        product.setProductName(productDto.getProductName());
        product.setProductDetail(productDto.getProductDetail());
        product.setBrand(productDto.getBrand());
        product.setAvailability(productDto.isAvailability());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setImageName(productDto.getImageName());
        product.setOriginalPrice(productDto.getOriginalPrice());
        product.setQuantity(productDto.getQuantity());

        product_repo.save(product);
        return mapper.map(product,ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product=product_repo.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product with given Id is not present"));

        String fullPath=imagePath+ File.separator+product.getImageName();
        try{
            Path path= Paths.get(fullPath);
            Files.deleteIfExists(path);
        }catch (IOException e){
            e.printStackTrace();
        }


        product_repo.delete(product);
        return;
    }

    @Override
    public ProductDto getSingleProduct(String productId) {
        Product product=product_repo.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product with given Id is not present"));
        return mapper.map(product,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=Sort.by(sortBy);
        if(sortDir.equalsIgnoreCase("desc"))
            sort=sort.descending();

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=product_repo.findAll(pageable);

        PageableResponse<ProductDto> resp= ToPageableResponse.getPageableResponse(page,ProductDto.class);
        return resp;
    }

    @Override
    public PageableResponse<ProductDto> getAvailableProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=Sort.by(sortBy);
        if(sortDir.equalsIgnoreCase("desc"))
            sort=sort.descending();

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=product_repo.findByAvailabilityTrue(pageable);

        PageableResponse<ProductDto> resp= ToPageableResponse.getPageableResponse(page,ProductDto.class);
        return resp;
    }

    @Override
    public PageableResponse<ProductDto> searchProduct(String name,int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=Sort.by(sortBy);
        if(sortDir.equalsIgnoreCase("desc"))
            sort=sort.descending();

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=product_repo.findByProductNameContaining(name,pageable);

        PageableResponse<ProductDto> resp= ToPageableResponse.getPageableResponse(page,ProductDto.class);
        return resp;
    }

    @Override
    public PageableResponse<ProductDto> getProductByBrand(String brand, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=Sort.by(sortBy);
        if(sortDir.equalsIgnoreCase("desc"))
            sort=sort.descending();

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=product_repo.findByBrand(brand,pageable);

        PageableResponse<ProductDto> resp= ToPageableResponse.getPageableResponse(page,ProductDto.class);
        return resp;
    }

    @Override
    public PageableResponse<ProductDto> getProductByPriceRange(int low, int high, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=Sort.by(sortBy);
        if(sortDir.equalsIgnoreCase("desc"))
            sort=sort.descending();

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=product_repo.findByDiscountedPriceBetween(low,high,pageable);

        PageableResponse<ProductDto> resp= ToPageableResponse.getPageableResponse(page,ProductDto.class);
        return resp;
    }

    @Override
    public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {
        String Id= UUID.randomUUID().toString();
        productDto.setProductId(Id);

        Date currDate=new Date();
        productDto.setAddeddate(currDate);

        Category category=category_repo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category with given Id is not present"));

        Product product1=mapper.map(productDto,Product.class);
        product1.setCategory(category);

        Product product=product_repo.save(product1);
        return mapper.map(product,ProductDto.class);
    }

    @Override
    public ProductDto assignProductToCategory(String productId, String categoryId) {
        Product product=product_repo.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product with given Id is not present"));
        Category category=category_repo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category with given Id is not present"));
        product.setCategory(category);

        Product savedProduct=product_repo.save(product);
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProductCategory(String categoryId, int pageNumber) {

        Pageable pageable=PageRequest.of(pageNumber,10);

        Category curr_category=category_repo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category with given CategoryId does not exist"));
        Page<Product> page=product_repo.findByCategory(curr_category,pageable);
        PageableResponse<ProductDto> resp=ToPageableResponse.getPageableResponse(page,ProductDto.class);
        return resp;
    }


}
