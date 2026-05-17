package com.openwebinars.todo.rest.service;

import com.openwebinars.todo.rest.dto.EditCategoryDto;
import com.openwebinars.todo.rest.error.CategoryNotFoundException;
import com.openwebinars.todo.rest.error.ValidationException;
import com.openwebinars.todo.rest.model.Category;
import com.openwebinars.todo.rest.repos.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public Category create(EditCategoryDto dto) {
        if (categoryRepository.existsByTitleIgnoreCase(dto.title()))
            throw new ValidationException("Ya existe una categoria con ese titulo");
        return categoryRepository.save(Category.builder().title(dto.title()).build());
    }

    public Category edit(Long id, EditCategoryDto dto) {
        Category c = findById(id);
        c.setTitle(dto.title());
        return categoryRepository.save(c);
    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id))
            throw new CategoryNotFoundException(id);
        categoryRepository.deleteById(id);
    }
}
