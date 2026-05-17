package com.openwebinars.todo.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openwebinars.todo.rest.model.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "Datos para crear o editar una tarea")
public record EditTaskDto(

        @NotBlank(message = "El titulo es obligatorio")
        @Size(min = 1, max = 100)
        @Schema(example = "Aprender Spring Boot")
        String title,

        @Size(max = 1000)
        @Schema(example = "Hacer el curso de OpenWebinars.")
        String description,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(example = "2026-12-31T23:59:59")
        LocalDateTime deadline,

        @Schema(example = "MEDIA")
        Priority priority,

        @Schema(description = "ID de la categoria (opcional)", example = "1")
        Long categoryId,

        @Schema(example = "false")
        Boolean completed
) {}
