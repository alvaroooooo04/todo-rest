package com.openwebinars.todo.rest.repos;

import com.openwebinars.todo.rest.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findFirstByTitleIgnoreCase(String title);

    boolean existsByTitleIgnoreCase(String title);
}
