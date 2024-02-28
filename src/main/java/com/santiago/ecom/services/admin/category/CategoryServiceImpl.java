package com.santiago.ecom.services.admin.category;


import com.santiago.ecom.dto.CategoryDto;
import com.santiago.ecom.entity.Category;
import com.santiago.ecom.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(CategoryDto categoryDto){

        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        return categoryRepository.save(category);
    }

public List<Category> getAllCategories(){
        return categoryRepository.findAll();
}

}
