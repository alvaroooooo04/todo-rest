package com.openwebinars.todo.rest.service;

import com.openwebinars.todo.rest.dto.EditProfileDto;
import com.openwebinars.todo.rest.dto.NewUserCommand;
import com.openwebinars.todo.rest.error.UserNotFoundException;
import com.openwebinars.todo.rest.error.ValidationException;
import com.openwebinars.todo.rest.model.User;
import com.openwebinars.todo.rest.model.UserRole;
import com.openwebinars.todo.rest.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio que centraliza toda la logica de negocio de usuarios:
 * alta, listado, modificacion de perfil, promocion y degradacion de rol.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // -------- ALTA Y LECTURA --------

    public User register(NewUserCommand cmd) {
        if (userRepository.existsByUsername(cmd.username()))
            throw new ValidationException("Ya existe un usuario con ese username");
        if (userRepository.existsByEmail(cmd.email()))
            throw new ValidationException("Ya existe un usuario con ese email");

        User user = User.builder()
                .username(cmd.username())
                .email(cmd.email())
                .password(passwordEncoder.encode(cmd.password()))
                .fullname(cmd.fullname())
                .role(UserRole.USER)
                .build();

        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    // -------- PROMOCION / DEGRADACION --------

    /** Convierte un USER en GESTOR. Solo lo invoca un ADMIN. */
    public User promoteToGestor(Long id) {
        User u = findById(id);
        if (u.getRole() == UserRole.ADMIN)
            throw new ValidationException("No se puede promocionar a un ADMIN");
        u.setRole(UserRole.GESTOR);
        return userRepository.save(u);
    }

    /** Convierte un GESTOR de nuevo en USER. Solo lo invoca un ADMIN. */
    public User demoteToUser(Long id) {
        User u = findById(id);
        if (u.getRole() != UserRole.GESTOR)
            throw new ValidationException("Solo se puede degradar a un usuario GESTOR");
        u.setRole(UserRole.USER);
        return userRepository.save(u);
    }

    // -------- ADMIN: CRUD COMPLETO DE USUARIO --------

    public void delete(Long id) {
        if (!userRepository.existsById(id))
            throw new UserNotFoundException(id);
        userRepository.deleteById(id);
    }

    // -------- PERFIL DEL PROPIO USUARIO --------

    public User updateProfile(User current, EditProfileDto dto) {
        if (dto.email() != null && !dto.email().equals(current.getEmail())) {
            if (userRepository.existsByEmail(dto.email()))
                throw new ValidationException("Ya existe un usuario con ese email");
            current.setEmail(dto.email());
        }
        if (dto.fullname() != null) {
            current.setFullname(dto.fullname());
        }
        if (dto.password() != null && !dto.password().isBlank()) {
            current.setPassword(passwordEncoder.encode(dto.password()));
        }
        return userRepository.save(current);
    }
}
