package com.edutech.auth_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // Habilita la seguridad a nivel de método, permitiendo usar anotaciones como @PreAuthorize
public class SecurityConfig {
    @Autowired
    private JwtRequestFilter jwtRequestFilter; // Filtro para manejar JWT en las solicitudes
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Configuración de seguridad HTTP
        http
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF para simplificar las pruebas
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/api/v1/roles",
                    "/api/v1/auth/login"
                ).permitAll() // Permite acceso a Swagger y documentación de la API
                .requestMatchers("/api/v1/users").permitAll() // Solo ADMINISTRADOR puede acceder a /api/v1/users
                .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build(); 
    }

}
