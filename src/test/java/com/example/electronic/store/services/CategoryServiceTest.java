package com.example.electronic.store.services;

import com.example.electronic.store.dtos.CategoryDto;
import com.example.electronic.store.dtos.PageableResponse;
import com.example.electronic.store.entities.Category;
import com.example.electronic.store.repositories.CategoryRepositories;
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
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;
    @MockitoBean
    private CategoryRepositories categoryRepo;
    @Autowired
    private ModelMapper mapper;

    @Test
    public void save_category_test(){
        CategoryDto categoryDto=CategoryDto.builder()
                .categoryId("smartphone")
                .categoryTitle("Smartphones")
                .categoryDetail("a mobile phone that performs many of the functions of a computer, typically having a touchscreen interface, internet access, and an operating system capable of running downloaded apps.")
                .categoryImage("smartphone.jpg")
                .build();

        Mockito.when(categoryRepo.save(Mockito.any())).thenReturn(mapper.map(categoryDto, Category.class));

        CategoryDto saved_category=categoryService.saveCategory(categoryDto);
        Assertions.assertNotNull(saved_category);
        System.out.println(saved_category);
    }

    @Test
    public void update_category_test(){
        CategoryDto oldCategoryDto=CategoryDto.builder()
                .categoryId("smartphone")
                .categoryTitle("Smartphones")
                .categoryDetail("a mobile phone that performs many of the functions of a computer, typically having a touchscreen interface, internet access, and an operating system capable of running downloaded apps.")
                .categoryImage("smartphone.jpg")
                .build();

        CategoryDto newCategoryDto=CategoryDto.builder()
                .categoryId("smartphone")
                .categoryTitle("Smartphones")
                .categoryDetail("Varieties of smooth smartphone with Upgraded and enhanced performance.")
                .categoryImage("smartphone.jpg")
                .build();

        Mockito.when(categoryRepo.findById("smartphone")).thenReturn(Optional.ofNullable(mapper.map(oldCategoryDto, Category.class)));
        Mockito.when(categoryRepo.save(Mockito.any())).thenReturn(mapper.map(newCategoryDto,Category.class));

        CategoryDto updated_category=categoryService.updateCategory(newCategoryDto.getCategoryId(),newCategoryDto);
        Assertions.assertEquals("Varieties of smooth smartphone with Upgraded and enhanced performance.",updated_category.getCategoryDetail());
        System.out.println(updated_category);

    }

    @Test
    public void delete_category_test(){
        Category category=Category.builder()
                .categoryId("smartphone")
                .categoryTitle("Smartphones")
                .categoryDetail("a mobile phone that performs many of the functions of a computer, typically having a touchscreen interface, internet access, and an operating system capable of running downloaded apps.")
                .categoryImage("smartphone.jpg")
                .build();

        Mockito.when(categoryRepo.findById(category.getCategoryId())).thenReturn(Optional.ofNullable(category));

        categoryService.deleteCategory(category.getCategoryId());
        Mockito.verify(categoryRepo,Mockito.times(1)).delete(category);
    }

    @Test
    public void get_all_Category_test(){
        List<Category> lst=new ArrayList<>();
        lst.add(Category.builder()
                .categoryId("smartphone")
                .categoryTitle("Smartphones")
                .categoryDetail("a mobile phone that performs many of the functions of a computer, typically having a touchscreen interface, internet access, and an operating system capable of running downloaded apps.")
                .categoryImage("smartphone.jpg")
                .build()
        );
        lst.add(Category.builder()
                .categoryId("laptops")
                .categoryTitle("Laptops")
                .categoryDetail("High Quality Laptops.")
                .categoryImage("laptops.jpg")
                .build()
        );
        lst.add(Category.builder()
                .categoryId("earphone")
                .categoryTitle("Earphone")
                .categoryDetail("Long lasting Earphones")
                .categoryImage("earphones.jpg")
                .build()
        );

        Page<Category> pagelst=new PageImpl<>(lst);
        Mockito.when(categoryRepo.findAll((Pageable) Mockito.any())).thenReturn(pagelst);

        PageableResponse<CategoryDto> resp=categoryService.getAllCategory(0,1,"categoryId","ARC");
        System.out.println(resp.getContent());
        Assertions.assertEquals(3,resp.getContent().size());
    }

    @Test
    public void get_Category_test(){
        Category category=Category.builder()
                .categoryId("smartphone")
                .categoryTitle("Smartphones")
                .categoryDetail("a mobile phone that performs many of the functions of a computer, typically having a touchscreen interface, internet access, and an operating system capable of running downloaded apps.")
                .categoryImage("smartphone.jpg")
                .build();

        Mockito.when(categoryRepo.findById(category.getCategoryId())).thenReturn(Optional.of(category));
        CategoryDto categoryDto=categoryService.getCategory(category.getCategoryId());
        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals("smartphone.jpg",categoryDto.getCategoryImage());
        System.out.println(categoryDto);
    }

}
