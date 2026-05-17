package com.openwebinars.todo.rest.error;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("No existe la categoria con ID %d".formatted(id));
    }
}
