package com.edutech.userprofile_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Configuración de seguridad HTTP
        http
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF para simplificar las pruebas
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/api/v1/usuario_perfil/perfiles",      
                    "/api/v1/usuario_perfil/perfiles/user",
                    "/api/v1/usuario_perfil/perfiles/nombre",
                    "/api/v1/usuario_perfil//perfiles/genero"
                ).permitAll() // Permite acceso a Swagger y documentación de la API
                .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
            );
        return http.build(); 
    }

}
