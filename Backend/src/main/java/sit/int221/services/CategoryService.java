package sit.int221.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sit.int221.dtos.CreateCategoryDTO;
import sit.int221.entities.Category;
import sit.int221.repositories.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    public Category createCategory(CreateCategoryDTO category) {
        Category newCategory = modelMapper.map(category, Category.class);
        return categoryRepository.saveAndFlush(newCategory);
    }
}