package com.example.electronic.store.services;

import com.example.electronic.store.dtos.PageableResponse;
import com.example.electronic.store.dtos.ProductDto;
import com.example.electronic.store.entities.Product;
import com.example.electronic.store.repositories.ProductRepositories;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @MockitoBean
    private ProductRepositories product_repo;
    @Autowired
    private ModelMapper mapper;

    @Test
    public void create_product_test(){
        ProductDto productDto =ProductDto.builder()
                .productId("123")
                .productName("Smartphone")
                .productDetail("Latest model with powerful features")
                .originalPrice(50000)
                .discountedPrice(45000)
                .quantity(20)
                .addeddate(new Date())
                .availability(true)
                .brand("TechBrand")
                .imageName("smartphone.jpg")
                .build();

        Mockito.when(product_repo.save(Mockito.any())).thenReturn(mapper.map(productDto, Product.class));
        ProductDto saved_productDto=productService.createProduct(productDto);
        Assertions.assertNotNull(saved_productDto);
        System.out.println(saved_productDto);
    }

    @Test
    public void updateProductTest(){
        Product product =Product.builder()
                .productId("123")
                .productName("Smartphone")
                .productDetail("Latest model with powerful features")
                .originalPrice(50000)
                .discountedPrice(45000)
                .quantity(20)
                .addeddate(new Date())
                .availability(true)
                .brand("TechBrand")
                .imageName("smartphone.jpg")
                .build();

        ProductDto newProductDto =ProductDto.builder()
                .productId("123")
                .productName("Laptops")
                .productDetail("Latest model with powerful features")
                .originalPrice(50000)
                .discountedPrice(45000)
                .quantity(20)
                .addeddate(new Date())
                .availability(true)
                .brand("TechBrand")
                .imageName("smartphone.jpg")
                .build();

        Mockito.when(product_repo.findById(Mockito.anyString())).thenReturn(Optional.of(product));
        ProductDto updatedProductDto=productService.updateProduct(newProductDto.getProductId(),newProductDto);
        Mockito.verify(product_repo,Mockito.times(1)).save(product);

        Assertions.assertNotNull(updatedProductDto);
        Assertions.assertEquals("Laptops",updatedProductDto.getProductName());
        System.out.println(updatedProductDto);
    }

    @Test
    public void delete_product_test(){
        Product product =Product.builder()
                .productId("123")
                .productName("Smartphone")
                .productDetail("Latest model with powerful features")
                .originalPrice(50000)
                .discountedPrice(45000)
                .quantity(20)
                .addeddate(new Date())
                .availability(true)
                .brand("TechBrand")
                .imageName("smartphone.jpg")
                .build();

        Mockito.when(product_repo.findById(product.getProductId())).thenReturn(Optional.of(product));
        productService.deleteProduct(product.getProductId());
        Mockito.verify(product_repo,Mockito.times(1)).delete(product);
    }

    @Test
    public void get_all_product_test(){
        Product product1 =Product.builder()
                .productId("123")
                .productName("Smartphone")
                .productDetail("Latest model with powerful features")
                .originalPrice(50000)
                .discountedPrice(45000)
                .quantity(20)
                .addeddate(new Date())
                .availability(true)
                .brand("TechBrand")
                .imageName("smartphone.jpg")
                .build();

        Product product2 =Product.builder()
                .productId("hsx")
                .productName("Laptops")
                .productDetail("Latest model with powerful features")
                .originalPrice(50000)
                .discountedPrice(45000)
                .quantity(20)
                .addeddate(new Date())
                .availability(true)
                .brand("TechBrand")
                .imageName("laptop.jpg")
                .build();

        List<Product> lst=new ArrayList<>();
        lst.add(product1);
        lst.add(product2);

        Page<Product> page=new PageImpl<>(lst);
        Mockito.when(product_repo.findAll((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<ProductDto> resp=productService.getAllProduct(0,1,"productId","ARC");

        Assertions.assertEquals(2,resp.getContent().size());
        System.out.println(resp.getContent());
    }

}
