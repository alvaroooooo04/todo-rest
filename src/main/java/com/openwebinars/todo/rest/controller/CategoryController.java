package com.openwebinars.todo.rest.controller;

import com.openwebinars.todo.rest.dto.CategoryDto;
import com.openwebinars.todo.rest.dto.EditCategoryDto;
import com.openwebinars.todo.rest.service.CategoryService;
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
 * CRUD de categorias.
 * - GET /category y GET /category/{id}: cualquier usuario autenticado
 * - POST, PUT, DELETE: solo ADMIN o GESTOR (configurado en SecurityConfig)
 */
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
@Tag(name = "Categorias", description = "Gestion de categorias (ADMIN y GESTOR). Lectura: cualquier usuario.")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Listar todas las categorias", description = "Accesible por cualquier usuario autenticado.")
    @ApiResponse(responseCode = "200", description = "Lista de categorias",
        content = @Content(mediaType = "application/json",
            examples = @ExampleObject("""
                [{"id":1,"title":"Trabajo"},{"id":2,"title":"Personal"},
                 {"id":3,"title":"Estudios"},{"id":4,"title":"Casa"}]
            """)))
    @GetMapping
    public List<CategoryDto> getAll() {
        return categoryService.findAll().stream().map(CategoryDto::of).toList();
    }

    @Operation(summary = "Obtener categoria por ID")
    @ApiResponse(responseCode = "200", description = "Categoria encontrada")
    @ApiResponse(responseCode = "404", description = "Categoria no encontrada")
    @GetMapping("/{id}")
    public CategoryDto getById(
            @Parameter(description = "ID de la categoria", example = "1") @PathVariable Long id) {
        return CategoryDto.of(categoryService.findById(id));
    }

    @Operation(summary = "Crear categoria", description = "Solo ADMIN o GESTOR.")
    @ApiResponse(responseCode = "201", description = "Categoria creada",
        content = @Content(schema = @Schema(implementation = CategoryDto.class),
            examples = @ExampleObject("{\"id\":5,\"title\":\"Deportes\"}")))
    @ApiResponse(responseCode = "400", description = "Titulo ya existe o datos invalidos")
    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody EditCategoryDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CategoryDto.of(categoryService.create(dto)));
    }

    @Operation(summary = "Editar categoria", description = "Solo ADMIN o GESTOR.")
    @ApiResponse(responseCode = "200", description = "Categoria actualizada")
    @ApiResponse(responseCode = "404", description = "Categoria no encontrada")
    @PutMapping("/{id}")
    public CategoryDto edit(
            @Parameter(description = "ID de la categoria", example = "1") @PathVariable Long id,
            @Valid @RequestBody EditCategoryDto dto) {
        return CategoryDto.of(categoryService.edit(id, dto));
    }

    @Operation(summary = "Eliminar categoria", description = "Solo ADMIN o GESTOR.")
    @ApiResponse(responseCode = "204", description = "Categoria eliminada")
    @ApiResponse(responseCode = "404", description = "Categoria no encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la categoria", example = "1") @PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
