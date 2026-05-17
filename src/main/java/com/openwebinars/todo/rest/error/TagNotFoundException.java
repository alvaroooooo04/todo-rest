package com.openwebinars.todo.rest.error;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(Long id) {
        super("No existe el tag con ID %d".formatted(id));
    }
}
