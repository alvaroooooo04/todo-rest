package com.openwebinars.todo.rest.repos;

import com.openwebinars.todo.rest.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findFirstByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
