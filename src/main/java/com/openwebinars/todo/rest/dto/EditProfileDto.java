package com.openwebinars.todo.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * Datos modificables por el propio usuario sobre su perfil.
 * Todos los campos son opcionales (null = no cambiar).
 */
@Schema(description = "Datos modificables del propio perfil de usuario")
public record EditProfileDto(
        @Email(message = "Debe ser un email valido")
        @Schema(example = "nuevo@email.com")
        String email,

        @Size(max = 100)
        @Schema(example = "Lorena Diaz Perez")
        String fullname,

        @Size(min = 4, max = 100, message = "La contrasena debe tener al menos 4 caracteres")
        @Schema(example = "nueva_password")
        String password
) {}
