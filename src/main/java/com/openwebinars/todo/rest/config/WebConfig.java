package com.openwebinars.todo.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuracion de CORS. Los origenes permitidos se pasan via property
 * {@code app.cors.allowed-origins} (separados por coma) para poder
 * personalizarlos en cada despliegue sin tocar el codigo.
 *
 * Por defecto se permiten los origenes habituales de desarrollo (Live Server).
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins:http://127.0.0.1:5500,http://localhost:5500,http://localhost:5173,http://localhost:9000}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
