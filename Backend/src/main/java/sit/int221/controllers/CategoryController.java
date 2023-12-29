package sit.int221.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import sit.int221.dtos.CreateCategoryDTO;
import sit.int221.entities.Category;
import sit.int221.services.CategoryService;

import java.util.List;
@RestController
@CrossOrigin(origins= {"http://localhost:5173", "https://intproj22.sit.kmutt.ac.th"})
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{categoryId}")
    public Category getCategory(@PathVariable Integer categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @PostMapping("")
    public Category createCategory(@Valid @RequestBody CreateCategoryDTO category, BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException((MethodParameter) null, bindingResult);
        }

        return categoryService.createCategory(category);
    }
}
