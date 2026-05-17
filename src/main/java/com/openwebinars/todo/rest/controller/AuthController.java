package com.openwebinars.todo.rest.controller;

import com.openwebinars.todo.rest.dto.NewUserCommand;
import com.openwebinars.todo.rest.dto.UserDto;
import com.openwebinars.todo.rest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Autenticacion", description = "Endpoints publicos de registro")
public class AuthController {

    private final UserService userService;

    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Crea un nuevo usuario con rol USER. No requiere autenticacion."
    )
    @ApiResponse(responseCode = "201", description = "Usuario creado",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserDto.class),
            examples = @ExampleObject("""
                {"id":4,"username":"maria","email":"maria@laboral.es","fullname":"Maria Lopez","role":"USER"}
            """)))
    @ApiResponse(responseCode = "400", description = "Datos invalidos o usuario/email ya existe")
    @PostMapping("/auth/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody NewUserCommand cmd) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserDto.of(userService.register(cmd)));
    }
}
