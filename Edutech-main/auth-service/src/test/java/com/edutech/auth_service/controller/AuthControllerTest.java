package com.edutech.auth_service.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.edutech.auth_service.model.Rol;
import com.edutech.auth_service.model.User;
import com.edutech.auth_service.service.RolService;
import com.edutech.auth_service.service.UserService;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad para pruebas
// Si se quiere probar la seguridad, se  elimina esta línea
public class AuthControllerTest {
    @MockBean  // Se usa MockBean para simular el servicio de rol
    private RolService rolService;
    @MockBean // Se usa MockBean para simular el servicio de usuario
    private UserService userService;

    @Autowired
    private MockMvc mockMvc; // Permite realizar peticiones HTTP a los endpoints del controlador

    @Test // Prueba: Obtener roles
    void obtenerRoles_returnOKAndJson() throws Exception { 
        Rol rol1 = new Rol();
        rol1.setId(1L);
        rol1.setNombre("Estudiante");
        Rol rol2 = new Rol();
        rol2.setId(2L);
        rol2.setNombre("Instructor");
        List<Rol> roles = Arrays.asList(rol1, rol2);
        when(rolService.obtenerRolOrdenPorId()).thenReturn(roles); // Simula el servicio para devolver una lista de roles

        mockMvc.perform(get("/api/v1/roles"))
               .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
               .andExpect(jsonPath("$[0].nombre").value("Estudiante")) 
               .andExpect(jsonPath("$[1].id").value(2)) 
               .andExpect(jsonPath("$[1].nombre").value("Instructor"));
    }

    @Test // Prueba: Crear rol
    void crearRol_returnCreatedAndJson() throws Exception {
    Rol entrada = new Rol();
    entrada.setNombre("Editor");

    Rol salida = new Rol();
    salida.setId(4L);
    salida.setNombre("Editor");

    when(rolService.crearRol(any(Rol.class))).thenReturn(salida); // Simula el servicio para devolver un rol creado

    mockMvc.perform(post("/api/v1/roles")
                .contentType("application/json")
                .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(entrada))) // Convierte el objeto Rol a JSON y lo envía en la petición
                .andExpect(status().isCreated()) // Verifica que el estado de la respuesta sea 201 Created
                .andExpect(jsonPath("$.id").value(4L))
                .andExpect(jsonPath("$.nombre").value("Editor"));
    }

    @Test // Prueba: Obtener rol por ID
    void eliminarRol_returnOKMessage() throws Exception {
    when(rolService.eliminarRol(4L)).thenReturn("Rol eliminado");

    mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/v1/roles/4")) // Realiza una petición DELETE al endpoint de eliminación de rol
                .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(jsonPath("$").value("Rol eliminado"));
    }

    @Test // Prueba: Crear rol con nombre vacío
    void crearRol_nombreVacio_returnBadRequest() throws Exception {
    Rol entrada = new Rol(); // nombre vacío

    when(rolService.crearRol(any(Rol.class)))
        .thenThrow(new RuntimeException("El nombre del rol es obligatorio"));

    mockMvc.perform(post("/api/v1/roles")
                .contentType("application/json")
                .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(entrada))) // Convierte el objeto Rol a JSON y lo envía en la petición
                .andExpect(status().isBadRequest()) // Verifica que el estado de la respuesta sea 400 Bad Request
                .andExpect(jsonPath("$").value("El nombre del rol es obligatorio"));
    }

    @Test // Prueba: Obtener usuarios
    void obtenerUsuarios_returnOKAndJson() throws Exception {
    // Crear usuarios simulados
        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("Administrador");

        User user1 = new User(1L, "Maria", "maria@ejemplo.com", "xxxx", null, null, rol);
        User user2 = new User(2L, "Rocio", "rocio@ejemplo.com", "yyyy", null, null, rol);

        List<User> usuarios = Arrays.asList(user1, user2);

    // Simular comportamiento del servicio
    when(userService.obtenerUser()).thenReturn(usuarios);

    // Realizar la petición y validar
    mockMvc.perform(get("/api/v1/users"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].username").value("Maria"))
               .andExpect(jsonPath("$[1].username").value("Rocio"));
    }

    @Test // Prueba: Crear usuario
    void crearUsuario_returnCreatedAndJson() throws Exception {
        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("Administrador");

        User entrada = new User(null, "Thomas", "thomas@ejemplo.com", "zzzz", null, null, rol);
        User salida = new User(null, "Thomas", "thomas@ejemplo.com", "zzzz", null, null, rol);

        when(userService.crearUser(any(String.class), any(String.class), any(String.class), any(Long.class))).thenReturn(salida);

        mockMvc.perform(post("/api/v1/users")
                .contentType("application/json")
                .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(entrada)))
            .andExpect(status().isCreated()) // Verifica que el estado de la respuesta sea 201 Created
            .andExpect(jsonPath("$.username").value("Thomas"))
            .andExpect(jsonPath("$.email").value("thomas@ejemplo.com"));
    }

    @Test // Prueba: Obtener usuario por ID
    void obtenerUserPorId_returnOKAndJson() throws Exception {
    // Simulamos un usuario
        User user = new User();
        user.setId(1L);
        user.setUsername("Thomas");
        user.setEmail("thomas@ejemplo.com");

        when(userService.obtenerUserPorId(1L)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/1"))
               .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
               .andExpect(jsonPath("$.username").value("Thomas"))
               .andExpect(jsonPath("$.email").value("thomas@ejemplo.com"));
    }

    @Test // Prueba: Obtener usuario por ID que no existe
    void obtenerUserPorId_usuarioNoExiste_return404() throws Exception {
        when(userService.obtenerUserPorId(99L)).thenThrow(new RuntimeException("Usuario no encontrado Id: 99"));

        mockMvc.perform(get("/api/v1/users/99"))
               .andExpect(status().isNotFound()) // Verifica que el estado de la respuesta sea 404 Not Found
               .andExpect(jsonPath("$").value("Usuario no encontrado Id: 99"));
    }

}
