package com.openwebinars.todo.rest.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Categoria global a la que puede pertenecer una tarea.
 * Las categorias son gestionadas por ADMIN y GESTOR. Cualquier
 * usuario puede consultarlas (listar categorias disponibles).
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;
}
