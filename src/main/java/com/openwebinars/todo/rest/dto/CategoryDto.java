package com.openwebinars.todo.rest.dto;

import com.openwebinars.todo.rest.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categoria de tareas")
public record CategoryDto(

        @Schema(description = "Identificador unico", example = "1")
        Long id,

        @Schema(description = "Nombre de la categoria", example = "Trabajo")
        String title
) {
    public static CategoryDto of(Category c) {
        return new CategoryDto(c.getId(), c.getTitle());
    }
}
