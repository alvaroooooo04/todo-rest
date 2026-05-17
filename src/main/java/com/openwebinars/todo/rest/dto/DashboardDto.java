package com.openwebinars.todo.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Estadisticas del dashboard del usuario autenticado")
public record DashboardDto(

        @Schema(description = "Total de tareas del usuario", example = "12")
        long total,

        @Schema(description = "Tareas completadas", example = "5")
        long completadas,

        @Schema(description = "Tareas pendientes", example = "7")
        long pendientes,

        @Schema(description = "Tareas vencidas (deadline anterior al momento actual y no completadas)", example = "2")
        long vencidas,

        @Schema(description = "Numero de tareas por prioridad",
                example = "{\"ALTA\": 2, \"MEDIA\": 6, \"BAJA\": 4}")
        Map<String, Long> porPrioridad,

        @Schema(description = "Numero de tareas por categoria (clave = titulo)",
                example = "{\"Trabajo\": 5, \"Personal\": 4, \"Estudios\": 3}")
        Map<String, Long> porCategoria
) {}
