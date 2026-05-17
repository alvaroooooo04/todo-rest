package com.openwebinars.todo.rest.error;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) { super("No existe el usuario con ID %d".formatted(id)); }
    public UserNotFoundException(String username) { super("No existe el usuario " + username); }
}
