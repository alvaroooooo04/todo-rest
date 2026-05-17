package com.openwebinars.todo.rest.dto;

import com.openwebinars.todo.rest.model.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Etiqueta de una tarea")
public record TagDto(

        @Schema(description = "Identificador unico", example = "1")
        Long id,

        @Schema(description = "Nombre de la etiqueta", example = "urgente")
        String name
) {
    public static TagDto of(Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }
}
