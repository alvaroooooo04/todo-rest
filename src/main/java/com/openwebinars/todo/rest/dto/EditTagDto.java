package com.openwebinars.todo.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear o editar una etiqueta")
public record EditTagDto(
        @NotBlank(message = "El nombre del tag es obligatorio")
        @Size(min = 2, max = 30, message = "El nombre debe tener entre 2 y 30 caracteres")
        @Schema(description = "Nombre del tag", example = "urgente")
        String name
) {}
