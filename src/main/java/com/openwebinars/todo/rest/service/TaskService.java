package com.openwebinars.todo.rest.service;

import com.openwebinars.todo.rest.dto.DashboardDto;
import com.openwebinars.todo.rest.dto.EditTaskDto;
import com.openwebinars.todo.rest.error.CategoryNotFoundException;
import com.openwebinars.todo.rest.error.TagNotFoundException;
import com.openwebinars.todo.rest.error.TaskNotFoundException;
import com.openwebinars.todo.rest.model.*;
import com.openwebinars.todo.rest.repos.CategoryRepository;
import com.openwebinars.todo.rest.repos.TagRepository;
import com.openwebinars.todo.rest.repos.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio principal del agregado Tarea. Concentra:
 *  - CRUD basico (todo restringido por autor)
 *  - Busquedas por todos los campos (titulo, descripcion, prioridad, deadline,
 *    completed, categoria, tags)
 *  - Operaciones sobre la coleccion de tags de una tarea (asignar / eliminar)
 *  - Calculo del dashboard de estadisticas del usuario
 */
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    // ============== LECTURAS ==============

    public List<Task> findByAuthor(User author) {
        return taskRepository.findByAuthor(author);
    }

    public Task findByIdAndAuthor(Long id, User author) {
        Task t = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        if (t.getAuthor() == null || !t.getAuthor().getId().equals(author.getId()))
            throw new TaskNotFoundException(id);
        return t;
    }

    // ============== ESCRITURAS ==============

    public Task save(EditTaskDto cmd, User author) {
        Task t = Task.builder()
                .title(cmd.title())
                .description(cmd.description())
                .deadline(cmd.deadline())
                .priority(cmd.priority() != null ? cmd.priority() : Priority.MEDIA)
                .completed(Boolean.TRUE.equals(cmd.completed()))
                .author(author)
                .build();

        if (cmd.categoryId() != null) {
            Category c = categoryRepository.findById(cmd.categoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(cmd.categoryId()));
            t.setCategory(c);
        }
        return taskRepository.save(t);
    }

    public Task edit(EditTaskDto cmd, Long id, User author) {
        Task t = findByIdAndAuthor(id, author);
        t.setTitle(cmd.title());
        t.setDescription(cmd.description());
        t.setDeadline(cmd.deadline());
        if (cmd.priority() != null) t.setPriority(cmd.priority());
        if (cmd.completed() != null) t.setCompleted(cmd.completed());

        if (cmd.categoryId() != null) {
            Category c = categoryRepository.findById(cmd.categoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(cmd.categoryId()));
            t.setCategory(c);
        } else {
            t.setCategory(null);
        }
        return taskRepository.save(t);
    }

    public void delete(Long id, User author) {
        Task t = findByIdAndAuthor(id, author);
        taskRepository.delete(t);
    }

    // ============== BUSQUEDAS ==============

    public List<Task> searchByTitle(User author, String title) {
        return taskRepository.findByAuthorAndTitleContainingIgnoreCase(author, title);
    }

    public List<Task> searchByDescription(User author, String description) {
        return taskRepository.findByAuthorAndDescriptionContainingIgnoreCase(author, description);
    }

    public List<Task> searchByCompleted(User author, boolean completed) {
        return taskRepository.findByAuthorAndCompleted(author, completed);
    }

    public List<Task> searchByCategory(User author, Long categoryId) {
        return taskRepository.findByAuthorAndCategoryId(author, categoryId);
    }

    public List<Task> searchByPriority(User author, Priority priority) {
        return taskRepository.findByAuthorAndPriority(author, priority);
    }

    public List<Task> searchByDeadlineBefore(User author, LocalDateTime fecha) {
        return taskRepository.findByAuthorAndDeadlineBefore(author, fecha);
    }

    public List<Task> searchByDeadlineBetween(User author, LocalDateTime desde, LocalDateTime hasta) {
        return taskRepository.findByAuthorAndDeadlineBetween(author, desde, hasta);
    }

    public List<Task> searchByTags(User author, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) return List.of();
        return taskRepository.findByAuthorAndAnyTagIn(author, tagIds);
    }

    // ============== GESTION DE TAGS EN UNA TAREA ==============

    public Task addTag(Long taskId, Long tagId, User author) {
        Task t = findByIdAndAuthor(taskId, author);
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new TagNotFoundException(tagId));
        t.getTags().add(tag);
        return taskRepository.save(t);
    }

    public Task removeTag(Long taskId, Long tagId, User author) {
        Task t = findByIdAndAuthor(taskId, author);
        t.getTags().removeIf(tag -> tag.getId().equals(tagId));
        return taskRepository.save(t);
    }

    // ============== DASHBOARD ==============

    public DashboardDto dashboard(User author) {
        List<Task> tareas = taskRepository.findByAuthor(author);

        long total = tareas.size();
        long completadas = tareas.stream().filter(Task::isCompleted).count();
        long pendientes = total - completadas;
        long vencidas = tareas.stream()
                .filter(t -> !t.isCompleted())
                .filter(t -> t.getDeadline() != null && t.getDeadline().isBefore(LocalDateTime.now()))
                .count();

        Map<String, Long> porPrioridad = tareas.stream().collect(Collectors.groupingBy(
                t -> t.getPriority() == null ? "SIN_PRIORIDAD" : t.getPriority().name(),
                Collectors.counting()));

        Map<String, Long> porCategoria = tareas.stream().collect(Collectors.groupingBy(
                t -> t.getCategory() == null ? "Sin categoria" : t.getCategory().getTitle(),
                Collectors.counting()));

        return new DashboardDto(total, completadas, pendientes, vencidas, porPrioridad, porCategoria);
    }
}
