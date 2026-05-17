package com.openwebinars.todo.rest.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejo centralizado de excepciones.
 * NO extiende ResponseEntityExceptionHandler para evitar conflictos
 * con los handlers que esa clase ya registra (MethodArgumentNotValidException, etc.)
 */
@RestControllerAdvice
public class GlobalErrorController {

    private static final String ERR_BASE = "https://laboral.es/errors/";

    // ---------- 404 ----------

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleTaskNotFound(TaskNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "Tarea no encontrada", ex.getMessage(), "task-not-found");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "Usuario no encontrado", ex.getMessage(), "user-not-found");
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleCategoryNotFound(CategoryNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "Categoria no encontrada", ex.getMessage(), "category-not-found");
    }

    @ExceptionHandler(TagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleTagNotFound(TagNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "Tag no encontrado", ex.getMessage(), "tag-not-found");
    }

    // ---------- 400 ----------

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleValidation(ValidationException ex) {
        return build(HttpStatus.BAD_REQUEST, "Error de validacion", ex.getMessage(), "validation");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleBeanValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }
        ProblemDetail pd = build(HttpStatus.BAD_REQUEST,
                "Datos no validos", "Hay errores en los datos enviados", "bean-validation");
        pd.setProperty("errors", fieldErrors);
        return pd;
    }

    // ---------- 401 / 403 ----------

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ProblemDetail handleAuth(AuthenticationException ex) {
        return build(HttpStatus.UNAUTHORIZED, "No autenticado", ex.getMessage(), "authentication");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "Acceso denegado", ex.getMessage(), "authorization");
    }

    // ---------- 500 generico ----------

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno", ex.getMessage(), "internal-error");
    }

    // ---------- helper ----------
    private ProblemDetail build(HttpStatus status, String title, String detail, String type) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setType(URI.create(ERR_BASE + type));
        return pd;
    }
}
