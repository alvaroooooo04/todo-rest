package com.openwebinars.todo.rest.error;

/**
 * Excepcion para errores de validacion de negocio detectados en los servicios
 * (por ejemplo, intentar registrar dos usuarios con el mismo email).
 * El handler global la traduce a HTTP 400.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) { super(message); }
}
