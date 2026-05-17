package com.openwebinars.todo.rest.model;

/**
 * Roles de usuario disponibles en el sistema.
 * <ul>
 *     <li>ADMIN: permisos totales. Puede gestionar usuarios y categorias.</li>
 *     <li>GESTOR: usuario promocionado por un ADMIN. Puede gestionar categorias.</li>
 *     <li>USER: usuario estandar. Gestiona sus propias tareas y tags.</li>
 * </ul>
 */
public enum UserRole {
    ADMIN,
    GESTOR,
    USER
}
