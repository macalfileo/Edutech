package com.edutech.auth_service.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.edutech.auth_service.model.Rol;
import com.edutech.auth_service.model.User;
import com.edutech.auth_service.repository.RolRepository;
import com.edutech.auth_service.repository.UserRepository;

@Configuration

public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(RolRepository rolRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            if(rolRepository.count() == 0) {
                // Cargar los roles
                Rol admin = new Rol();
                admin.setNombre("Administrador");
                rolRepository.save(admin);

                Rol estudiante = new Rol();
                estudiante.setNombre("Estudiante");
                rolRepository.save(estudiante);

                Rol instructor = new Rol();
                instructor.setNombre("Instructor");
                rolRepository.save(instructor);

                System.out.println("Roles iniciales cargados correctamente");  

            }

            if (userRepository.count() == 0) {
                Rol admin = rolRepository.findByNombre("Administrador");
                String pass = passwordEncoder().encode("admin1234");
                userRepository.save(new User(null, "Maria", "maria@ejemplo.com", pass, null, null, admin));
                userRepository.save(new User(null, "Rocio", "rocio@ejemplo.com", pass, null, null, admin));
                userRepository.save(new User(null, "Angelo", "angelo@ejemplo.com", pass, null, null, admin));
                
                System.out.println("Usuarios iniciales cargados correctamente");
            }else{
                System.out.println("Datos ya existen. No se cargaron nuevos datos");
            }
        };
    }

    @Bean // Encriptacion de contraseñas
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
