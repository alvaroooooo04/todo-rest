package com.openwebinars.todo.rest.repos;

import com.openwebinars.todo.rest.model.Priority;
import com.openwebinars.todo.rest.model.Task;
import com.openwebinars.todo.rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio de tareas. Las consultas estan restringidas al autor para
 * que cada usuario solo pueda ver y manipular sus propias tareas.
 *
 * El enunciado pide "consultas por cada uno de los campos propios de la tarea"
 * y "al menos una busqueda por cada elemento anadido a tarea no incluido en el
 * modelo inicial" (deadline y priority).
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Listado por autor
    List<Task> findByAuthor(User author);

    // Por titulo (contains, case insensitive)
    List<Task> findByAuthorAndTitleContainingIgnoreCase(User author, String title);

    // Por descripcion
    List<Task> findByAuthorAndDescriptionContainingIgnoreCase(User author, String description);

    // Por estado completed
    List<Task> findByAuthorAndCompleted(User author, boolean completed);

    // Por categoria
    List<Task> findByAuthorAndCategoryId(User author, Long categoryId);

    // ---- Busquedas asociadas a los atributos extra de la tarea ----

    // Por prioridad
    List<Task> findByAuthorAndPriority(User author, Priority priority);

    // Por deadline antes de una fecha (tareas vencidas o proximas a vencer)
    List<Task> findByAuthorAndDeadlineBefore(User author, LocalDateTime fecha);

    // Por deadline entre dos fechas
    List<Task> findByAuthorAndDeadlineBetween(User author, LocalDateTime desde, LocalDateTime hasta);

    // ---- Busqueda por tags (JPQL) ----
    @Query("""
            SELECT DISTINCT t FROM Task t
            JOIN t.tags tag
            WHERE t.author = :author
              AND tag.id IN :tagIds
            """)
    List<Task> findByAuthorAndAnyTagIn(@Param("author") User author,
                                       @Param("tagIds") List<Long> tagIds);

    // ---- Estadisticas para el dashboard ----
    long countByAuthor(User author);

    long countByAuthorAndCompleted(User author, boolean completed);

    long countByAuthorAndPriority(User author, Priority priority);

    long countByAuthorAndDeadlineBefore(User author, LocalDateTime fecha);
}
