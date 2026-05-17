package com.openwebinars.todo.rest.error;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) { super(message); }
    public TaskNotFoundException(Long id)        { super("No hay una tarea con ID %d".formatted(id)); }
    public TaskNotFoundException()               { super("No hay tareas con esos criterios"); }
}
