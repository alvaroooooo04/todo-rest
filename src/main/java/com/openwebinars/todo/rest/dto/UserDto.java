package com.openwebinars.todo.rest.dto;

import com.openwebinars.todo.rest.model.User;
import com.openwebinars.todo.rest.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representacion publica de un usuario. Nunca expone la password.
 */
@Schema(description = "Datos publicos de un usuario")
public record UserDto(
        @Schema(description = "Identificador", example = "1")
        Long id,
        @Schema(description = "Nombre de usuario unico", example = "lorena")
        String username,
        @Schema(description = "Correo electronico", example = "lorena@laboral.es")
        String email,
        @Schema(description = "Nombre completo", example = "Lorena Diaz")
        String fullname,
        @Schema(description = "Rol del usuario", example = "USER")
        UserRole role
) {
    public static UserDto of(User u) {
        return new UserDto(u.getId(), u.getUsername(), u.getEmail(),
                u.getFullname(), u.getRole());
    }
}
