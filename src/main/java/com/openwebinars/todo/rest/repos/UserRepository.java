package com.openwebinars.todo.rest.repos;

import com.openwebinars.todo.rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
