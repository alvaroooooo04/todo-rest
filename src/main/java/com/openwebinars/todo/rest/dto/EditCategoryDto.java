package com.openwebinars.todo.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear o editar una categoria")
public record EditCategoryDto(
        @NotBlank(message = "El titulo de la categoria es obligatorio")
        @Size(min = 2, max = 50, message = "El titulo debe tener entre 2 y 50 caracteres")
        @Schema(description = "Titulo de la categoria", example = "Trabajo")
        String title
) {}
