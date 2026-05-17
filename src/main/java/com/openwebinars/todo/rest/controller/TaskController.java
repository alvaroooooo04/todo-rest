package com.openwebinars.todo.rest.controller;

import com.openwebinars.todo.rest.dto.DashboardDto;
import com.openwebinars.todo.rest.dto.EditTaskDto;
import com.openwebinars.todo.rest.dto.GetTaskDto;
import com.openwebinars.todo.rest.model.Priority;
import com.openwebinars.todo.rest.model.User;
import com.openwebinars.todo.rest.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CRUD de tareas + busquedas + gestion de tags + dashboard.
 * Todas las operaciones estan restringidas al usuario autenticado:
 * cada usuario solo puede ver y manipular sus propias tareas.
 */
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
@Tag(name = "Tareas", description = "CRUD, busquedas avanzadas, gestion de tags y dashboard del usuario autenticado.")
public class TaskController {

    private final TaskService taskService;

    /* ============================================================
     *  CRUD basico
     * ============================================================ */

    @Operation(
        summary = "Listar todas mis tareas",
        description = "Devuelve todas las tareas del usuario autenticado."
    )
    @ApiResponse(responseCode = "200", description = "Lista de tareas",
        content = @Content(mediaType = "application/json",
            examples = @ExampleObject("""
                [{"id":1,"title":"Comprar alimentos","description":"Hacer la lista de la compra semanal.",
                  "completed":false,"createdAt":"2026-01-01T10:00:00","deadline":"2026-01-08T10:00:00",
                  "priority":"MEDIA","author":{"id":3,"username":"lorena","email":"lorena@laboral.es",
                  "fullname":"Lorena Diaz","role":"USER"},"category":{"id":2,"title":"Personal"},
                  "tags":[{"id":3,"name":"compras"}]}]
            """)))
    @GetMapping
    public List<GetTaskDto> getAll(@AuthenticationPrincipal User me) {
        return taskService.findByAuthor(me).stream().map(GetTaskDto::of).toList();
    }

    @Operation(summary = "Obtener tarea por ID", description = "Solo el propietario puede consultar la tarea.")
    @ApiResponse(responseCode = "200", description = "Tarea encontrada")
    @ApiResponse(responseCode = "404", description = "Tarea no encontrada o no pertenece al usuario")
    @GetMapping("/{id}")
    public GetTaskDto getById(
            @Parameter(description = "ID de la tarea", example = "1") @PathVariable Long id,
            @AuthenticationPrincipal User me) {
        return GetTaskDto.of(taskService.findByIdAndAuthor(id, me));
    }

    @Operation(
        summary = "Crear tarea",
        description = "Crea una nueva tarea asociada al usuario autenticado."
    )
    @ApiResponse(responseCode = "201", description = "Tarea creada",
        content = @Content(schema = @Schema(implementation = GetTaskDto.class),
            examples = @ExampleObject("""
                {"title":"Aprender Spring Boot",
                 "description":"Hacer el curso de OpenWebinars.",
                 "deadline":"2026-12-31T23:59:59","priority":"ALTA","categoryId":3,"completed":false}
            """)))
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    @PostMapping
    public ResponseEntity<GetTaskDto> create(
            @Valid @RequestBody EditTaskDto cmd,
            @AuthenticationPrincipal User me) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetTaskDto.of(taskService.save(cmd, me)));
    }

    @Operation(
        summary = "Editar tarea",
        description = "Edita una tarea existente. Solo el propietario puede editarla."
    )
    @ApiResponse(responseCode = "200", description = "Tarea actualizada")
    @ApiResponse(responseCode = "403", description = "No es el propietario de la tarea")
    @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    @PutMapping("/{id}")
    public GetTaskDto edit(
            @Parameter(description = "ID de la tarea", example = "1") @PathVariable Long id,
            @Valid @RequestBody EditTaskDto cmd,
            @AuthenticationPrincipal User me) {
        return GetTaskDto.of(taskService.edit(cmd, id, me));
    }

    @Operation(summary = "Eliminar tarea", description = "Solo el propietario puede eliminar la tarea.")
    @ApiResponse(responseCode = "204", description = "Tarea eliminada")
    @ApiResponse(responseCode = "404", description = "Tarea no encontrada o no pertenece al usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la tarea", example = "1") @PathVariable Long id,
            @AuthenticationPrincipal User me) {
        taskService.delete(id, me);
        return ResponseEntity.noContent().build();
    }

    /* ============================================================
     *  Busquedas (por cada campo de la tarea)
     * ============================================================ */

    @Operation(
        summary = "Buscar tareas por titulo",
        description = "Busqueda parcial e insensible a mayusculas por el titulo."
    )
    @ApiResponse(responseCode = "200", description = "Tareas coincidentes")
    @GetMapping("/search/title")
    public List<GetTaskDto> searchByTitle(
            @Parameter(description = "Fragmento del titulo", example = "comprar")
            @RequestParam String q,
            @AuthenticationPrincipal User me) {
        return taskService.searchByTitle(me, q).stream().map(GetTaskDto::of).toList();
    }

    @Operation(summary = "Buscar tareas por descripcion", description = "Busqueda parcial en la descripcion.")
    @ApiResponse(responseCode = "200", description = "Tareas coincidentes")
    @GetMapping("/search/description")
    public List<GetTaskDto> searchByDescription(
            @Parameter(description = "Fragmento de la descripcion", example = "lista")
            @RequestParam String q,
            @AuthenticationPrincipal User me) {
        return taskService.searchByDescription(me, q).stream().map(GetTaskDto::of).toList();
    }

    @Operation(summary = "Filtrar tareas por estado (completada / pendiente)")
    @ApiResponse(responseCode = "200", description = "Tareas filtradas")
    @GetMapping("/search/completed")
    public List<GetTaskDto> searchByCompleted(
            @Parameter(description = "true = completadas, false = pendientes", example = "false")
            @RequestParam boolean value,
            @AuthenticationPrincipal User me) {
        return taskService.searchByCompleted(me, value).stream().map(GetTaskDto::of).toList();
    }

    @Operation(summary = "Filtrar tareas por categoria")
    @ApiResponse(responseCode = "200", description = "Tareas de la categoria")
    @GetMapping("/search/category/{categoryId}")
    public List<GetTaskDto> searchByCategory(
            @Parameter(description = "ID de la categoria", example = "1")
            @PathVariable Long categoryId,
            @AuthenticationPrincipal User me) {
        return taskService.searchByCategory(me, categoryId).stream().map(GetTaskDto::of).toList();
    }

    @Operation(
        summary = "Filtrar tareas por prioridad",
        description = "Atributo extra del modelo. Valores: BAJA, MEDIA, ALTA."
    )
    @ApiResponse(responseCode = "200", description = "Tareas con esa prioridad")
    @GetMapping("/search/priority/{priority}")
    public List<GetTaskDto> searchByPriority(
            @Parameter(description = "Nivel de prioridad", example = "ALTA")
            @PathVariable Priority priority,
            @AuthenticationPrincipal User me) {
        return taskService.searchByPriority(me, priority).stream().map(GetTaskDto::of).toList();
    }

    @Operation(
        summary = "Buscar tareas vencidas (deadline anterior a ahora)",
        description = "Atributo extra del modelo. Devuelve tareas cuyo deadline ya ha pasado."
    )
    @ApiResponse(responseCode = "200", description = "Tareas vencidas")
    @GetMapping("/search/overdue")
    public List<GetTaskDto> searchOverdue(@AuthenticationPrincipal User me) {
        return taskService.searchByDeadlineBefore(me, LocalDateTime.now())
                .stream().map(GetTaskDto::of).toList();
    }

    @Operation(
        summary = "Buscar tareas por rango de deadline",
        description = "Devuelve tareas cuyo deadline esta entre 'desde' y 'hasta'."
    )
    @ApiResponse(responseCode = "200", description = "Tareas en el rango")
    @GetMapping("/search/deadline")
    public List<GetTaskDto> searchByDeadline(
            @Parameter(description = "Fecha inicio (yyyy-MM-dd'T'HH:mm:ss)", example = "2026-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @Parameter(description = "Fecha fin (yyyy-MM-dd'T'HH:mm:ss)", example = "2026-12-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @AuthenticationPrincipal User me) {
        return taskService.searchByDeadlineBetween(me, desde, hasta)
                .stream().map(GetTaskDto::of).toList();
    }

    @Operation(
        summary = "Buscar tareas que contengan alguno de los tags indicados",
        description = "Se pasan los IDs de los tags como lista separada por comas."
    )
    @ApiResponse(responseCode = "200", description = "Tareas que tienen al menos un tag de los indicados")
    @GetMapping("/search/tags")
    public List<GetTaskDto> searchByTags(
            @Parameter(description = "Lista de IDs de tags", example = "1,2")
            @RequestParam List<Long> ids,
            @AuthenticationPrincipal User me) {
        return taskService.searchByTags(me, ids).stream().map(GetTaskDto::of).toList();
    }

    /* ============================================================
     *  Gestion de tags en una tarea concreta
     * ============================================================ */

    @Operation(
        summary = "Asignar tag a una tarea",
        description = "Anade un tag existente a la lista de tags de la tarea."
    )
    @ApiResponse(responseCode = "200", description = "Tarea con el tag anadido")
    @ApiResponse(responseCode = "404", description = "Tarea o tag no encontrado")
    @PostMapping("/{taskId}/tags/{tagId}")
    public GetTaskDto addTag(
            @Parameter(description = "ID de la tarea", example = "1") @PathVariable Long taskId,
            @Parameter(description = "ID del tag", example = "2")   @PathVariable Long tagId,
            @AuthenticationPrincipal User me) {
        return GetTaskDto.of(taskService.addTag(taskId, tagId, me));
    }

    @Operation(
        summary = "Eliminar tag de una tarea",
        description = "Retira un tag de la lista de tags de la tarea."
    )
    @ApiResponse(responseCode = "200", description = "Tarea sin el tag")
    @ApiResponse(responseCode = "404", description = "Tarea o tag no encontrado")
    @DeleteMapping("/{taskId}/tags/{tagId}")
    public GetTaskDto removeTag(
            @Parameter(description = "ID de la tarea", example = "1") @PathVariable Long taskId,
            @Parameter(description = "ID del tag", example = "2")   @PathVariable Long tagId,
            @AuthenticationPrincipal User me) {
        return GetTaskDto.of(taskService.removeTag(taskId, tagId, me));
    }

    /* ============================================================
     *  Dashboard
     * ============================================================ */

    @Operation(
        summary = "Dashboard de estadisticas",
        description = "Devuelve un resumen de las tareas del usuario: total, completadas, "
                    + "pendientes, vencidas, desglose por prioridad y por categoria."
    )
    @ApiResponse(responseCode = "200", description = "Estadisticas del usuario",
        content = @Content(schema = @Schema(implementation = DashboardDto.class),
            examples = @ExampleObject("""
                {"total":5,"completadas":1,"pendientes":4,"vencidas":1,
                 "porPrioridad":{"ALTA":2,"MEDIA":2,"BAJA":1},
                 "porCategoria":{"Trabajo":2,"Personal":2,"Estudios":1}}
            """)))
    @GetMapping("/dashboard")
    public DashboardDto dashboard(@AuthenticationPrincipal User me) {
        return taskService.dashboard(me);
    }
}
