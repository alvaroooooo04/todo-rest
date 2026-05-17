package com.openwebinars.todo.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Comando para registrar un nuevo usuario")
public record NewUserCommand(
        @NotBlank(message = "El username es obligatorio")
        @Size(min = 3, max = 30)
        @Schema(example = "lorena")
        String username,

        @NotBlank @Email(message = "Debe ser un email valido")
        @Schema(example = "lorena@laboral.es")
        String email,

        @NotBlank
        @Size(min = 4, max = 100, message = "La contrasena debe tener al menos 4 caracteres")
        @Schema(example = "1234")
        String password,

        @Schema(example = "Lorena Diaz")
        String fullname
) {}
