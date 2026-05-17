package com.openwebinars.todo.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openwebinars.todo.rest.model.Priority;
import com.openwebinars.todo.rest.model.Task;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Representacion publica de una tarea")
public record GetTaskDto(

        @Schema(description = "Identificador unico", example = "1") Long id,

        @Schema(example = "Comprar alimentos") String title,

        @Schema(example = "Hacer la lista de la compra semanal.") String description,

        @Schema(example = "false") boolean completed,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime deadline,

        @Schema(description = "Prioridad de la tarea", example = "MEDIA") Priority priority,

        UserDto author,

        CategoryDto category,

        List<TagDto> tags
) {
    public static GetTaskDto of(Task t) {
        return new GetTaskDto(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.isCompleted(),
                t.getCreatedAt(),
                t.getDeadline(),
                t.getPriority(),
                t.getAuthor() != null ? UserDto.of(t.getAuthor()) : null,
                t.getCategory() != null ? CategoryDto.of(t.getCategory()) : null,
                t.getTags() == null ? List.of()
                        : t.getTags().stream().map(TagDto::of).toList()
        );
    }
}
