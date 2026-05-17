package com.openwebinars.todo.rest.security;

import com.openwebinars.todo.rest.error.CustomAccessDeniedHandler;
import com.openwebinars.todo.rest.error.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion central de Spring Security:
 *  - Autenticacion HTTP Basic
 *  - Politica STATELESS (sin sesion en el servidor)
 *  - CORS gestionado en WebConfig
 *  - Reglas de acceso por endpoint y rol
 *  - Reglas finas declaradas con @PreAuthorize en los controladores
 *    (esta clase se activa con @EnableMethodSecurity)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(e -> {
                    e.accessDeniedHandler(accessDeniedHandler);
                    e.authenticationEntryPoint(authenticationEntryPoint);
                })
                .authorizeHttpRequests(authz -> authz
                        // ----- endpoints publicos -----
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ----- categorias -----
                        // listar categorias: cualquier autenticado
                        .requestMatchers(HttpMethod.GET, "/category", "/category/**").authenticated()
                        // gestionar categorias: ADMIN o GESTOR
                        .requestMatchers(HttpMethod.POST,   "/category/**").hasAnyRole("ADMIN", "GESTOR")
                        .requestMatchers(HttpMethod.PUT,    "/category/**").hasAnyRole("ADMIN", "GESTOR")
                        .requestMatchers(HttpMethod.DELETE, "/category/**").hasAnyRole("ADMIN", "GESTOR")

                        // ----- gestion de usuarios: solo ADMIN -----
                        .requestMatchers("/user/**").hasRole("ADMIN")

                        // ----- resto: autenticado -----
                        .anyRequest().authenticated()
                );

        // necesario para que la consola H2 funcione
        http.headers(h -> h.frameOptions(f -> f.disable()));

        return http.build();
    }
}
