package com.openwebinars.todo.rest.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Etiqueta que un usuario puede asociar a sus tareas.
 * La relacion con Task es N a N gestionada desde Task.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
}
