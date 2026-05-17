package com.openwebinars.todo.rest.controller;

import com.openwebinars.todo.rest.dto.EditTagDto;
import com.openwebinars.todo.rest.dto.TagDto;
import com.openwebinars.todo.rest.service.TagService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD de Tags (etiquetas). Cualquier usuario autenticado puede gestionar tags.
 * La asociacion de tags a tareas se realiza desde el TaskController.
 */
@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
@Tag(name = "Tags", description = "CRUD de etiquetas. Cualquier usuario autenticado.")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "Listar todos los tags")
    @ApiResponse(responseCode = "200", description = "Lista de tags",
        content = @Content(mediaType = "application/json",
            examples = @ExampleObject("""
                [{"id":1,"name":"urgente"},{"id":2,"name":"reunion"},
                 {"id":3,"name":"compras"},{"id":4,"name":"salud"}]
            """)))
    @GetMapping
    public List<TagDto> getAll() {
        return tagService.findAll().stream().map(TagDto::of).toList();
    }

    @Operation(summary = "Obtener tag por ID")
    @ApiResponse(responseCode = "200", description = "Tag encontrado")
    @ApiResponse(responseCode = "404", description = "Tag no encontrado")
    @GetMapping("/{id}")
    public TagDto getById(
            @Parameter(description = "ID del tag", example = "1") @PathVariable Long id) {
        return TagDto.of(tagService.findById(id));
    }

    @Operation(summary = "Crear tag")
    @ApiResponse(responseCode = "201", description = "Tag creado",
        content = @Content(schema = @Schema(implementation = TagDto.class),
            examples = @ExampleObject("{\"id\":5,\"name\":\"importante\"}")))
    @ApiResponse(responseCode = "400", description = "Nombre ya existe o invalido")
    @PostMapping
    public ResponseEntity<TagDto> create(@Valid @RequestBody EditTagDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TagDto.of(tagService.create(dto)));
    }

    @Operation(summary = "Editar tag")
    @ApiResponse(responseCode = "200", description = "Tag actualizado")
    @ApiResponse(responseCode = "404", description = "Tag no encontrado")
    @PutMapping("/{id}")
    public TagDto edit(
            @Parameter(description = "ID del tag", example = "1") @PathVariable Long id,
            @Valid @RequestBody EditTagDto dto) {
        return TagDto.of(tagService.edit(id, dto));
    }

    @Operation(summary = "Eliminar tag")
    @ApiResponse(responseCode = "204", description = "Tag eliminado")
    @ApiResponse(responseCode = "404", description = "Tag no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del tag", example = "1") @PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
