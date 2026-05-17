package com.openwebinars.todo.rest.controller;

import com.openwebinars.todo.rest.dto.EditProfileDto;
import com.openwebinars.todo.rest.dto.UserDto;
import com.openwebinars.todo.rest.model.User;
import com.openwebinars.todo.rest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Gestión de usuarios.
 * - /user/**         → solo ADMIN (configurado en SecurityConfig)
 * - /auth/me y /auth/me/edit → cualquier usuario autenticado (su propio perfil)
 */
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
@Tag(name = "Usuarios", description = "Gestion de usuarios (ADMIN) y perfil propio")
public class UserController {

    private final UserService userService;

    /* ================================================================
     *  ADMIN: listado y gestión completa de usuarios
     * ================================================================ */

    @Operation(summary = "Listar todos los usuarios", description = "Solo ADMIN.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios")
    @ApiResponse(responseCode = "403", description = "Sin permiso")
    @GetMapping("/user")
    public List<UserDto> getAll() {
        return userService.findAll().stream().map(UserDto::of).toList();
    }

    @Operation(summary = "Obtener usuario por ID", description = "Solo ADMIN.")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "No existe")
    @GetMapping("/user/{id}")
    public UserDto getById(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable Long id) {
        return UserDto.of(userService.findById(id));
    }

    @Operation(
        summary = "Promocionar usuario a GESTOR",
        description = "Convierte un usuario con rol USER en GESTOR. Solo ADMIN."
    )
    @ApiResponse(responseCode = "200", description = "Usuario promocionado a GESTOR")
    @ApiResponse(responseCode = "400", description = "El usuario ya es ADMIN o GESTOR")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PutMapping("/user/{id}/promote")
    public UserDto promote(
            @Parameter(description = "ID del usuario a promocionar", example = "3")
            @PathVariable Long id) {
        return UserDto.of(userService.promoteToGestor(id));
    }

    @Operation(
        summary = "Degradar GESTOR a USER",
        description = "Revierte el rol de un GESTOR a USER. Solo ADMIN."
    )
    @ApiResponse(responseCode = "200", description = "Usuario degradado a USER")
    @ApiResponse(responseCode = "400", description = "El usuario no es GESTOR")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PutMapping("/user/{id}/demote")
    public UserDto demote(
            @Parameter(description = "ID del usuario a degradar", example = "2")
            @PathVariable Long id) {
        return UserDto.of(userService.demoteToUser(id));
    }

    @Operation(summary = "Eliminar usuario", description = "Solo ADMIN.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del usuario a eliminar", example = "3")
            @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /* ================================================================
     *  Perfil del usuario autenticado (cualquier rol)
     * ================================================================ */

    @Operation(summary = "Ver mi perfil", description = "Devuelve el perfil del usuario autenticado.")
    @ApiResponse(responseCode = "200", description = "Perfil del usuario")
    @GetMapping("/auth/me")
    public UserDto getMe(@AuthenticationPrincipal User me) {
        return UserDto.of(me);
    }

    @Operation(
        summary = "Editar mi perfil",
        description = "Permite al usuario autenticado cambiar su email, nombre completo o contraseña."
    )
    @ApiResponse(responseCode = "200", description = "Perfil actualizado",
        content = @Content(schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "400", description = "Email ya en uso o datos invalidos")
    @PutMapping("/auth/me")
    public UserDto editMe(
            @AuthenticationPrincipal User me,
            @Valid @RequestBody EditProfileDto dto) {
        return UserDto.of(userService.updateProfile(me, dto));
    }
}
