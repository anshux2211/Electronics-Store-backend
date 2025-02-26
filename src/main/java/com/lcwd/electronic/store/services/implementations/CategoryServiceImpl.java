package com.lcwd.electronic.store.services.implementations;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.ToPageableResponse;
import com.lcwd.electronic.store.repositories.CategoryRepositories;
import com.lcwd.electronic.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepositories category_repo;
    @Autowired
    private ModelMapper mapper;
    @Value("${category.image.path}")
    private String imagePath;

    @Override
    public CategoryDto saveCategory(CategoryDto categoryDto) {
        if(categoryDto.getCategoryId()==null) {
            String id = UUID.randomUUID().toString();
            categoryDto.setCategoryId(id);
        }
        Category savedCategory=category_repo.save(mapper.map(categoryDto, Category.class));
        return mapper.map(savedCategory,CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(String categoryId, CategoryDto categoryDto) {
        Category oldCategory=category_repo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category with given ID is not present !!"));
        oldCategory.setCategoryDetail(categoryDto.getCategoryDetail());
        oldCategory.setCategoryTitle(categoryDto.getCategoryTitle());
        oldCategory.setCategoryImage(categoryDto.getCategoryImage());

        Category category=category_repo.save(oldCategory);
        return mapper.map(category,CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        Category category=category_repo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category with given Id is not present"));

        String fullPath=imagePath+ File.separator+category.getCategoryImage();

        try{
            Path path= Paths.get(fullPath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        category_repo.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=Sort.by(sortBy);
        if(sortDir.equalsIgnoreCase("desc"))
                sort=sort.descending();

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> categoryPage=category_repo.findAll(pageable);
        PageableResponse<CategoryDto> resp= ToPageableResponse.getPageableResponse(categoryPage,CategoryDto.class);
        return resp;
    }


    @Override
    public CategoryDto getCategory(String categoryId) {
        Category category=category_repo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category with given ID is not present"));
        return mapper.map(category,CategoryDto.class);
    }
}
