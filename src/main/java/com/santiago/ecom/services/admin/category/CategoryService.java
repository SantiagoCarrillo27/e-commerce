package com.santiago.ecom.services.admin.category;

import com.santiago.ecom.dto.CategoryDto;
import com.santiago.ecom.entity.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(CategoryDto categoryDto);

    List<Category> getAllCategories();
}
