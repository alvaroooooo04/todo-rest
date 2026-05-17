package com.openwebinars.todo.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
        title = "To Do List API",
        description = "API REST de gestion de tareas con categorias, tags y roles. " +
                "Proyecto Intermodular del CFGS Desarrollo de Aplicaciones Web - CIFP La Laboral.",
        version = "1.0",
        contact = @Contact(name = "CIFP La Laboral", email = "info@laboral.es"),
        license = @License(name = "CC BY")
))
@SpringBootApplication
public class TodoRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TodoRestApplication.class, args);
    }
}
