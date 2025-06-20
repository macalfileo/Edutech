package com.edutech.auth_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edutech.auth_service.model.Rol;
import com.edutech.auth_service.model.User;
import com.edutech.auth_service.service.RolService;
import com.edutech.auth_service.service.UserService;

@RestController // Maneja peticiones HTTP
@RequestMapping("/api/v1") // Define la ruta base
public class AuthController {
    @Autowired // Inyecta el Service para para usar sus métodos
    private UserService userService;

    @Autowired // Inyecta el Service para para usar sus métodos
    private RolService rolService;

    // Endpoint para consultar los roles
    @GetMapping("/roles")
    public ResponseEntity<List<Rol>> getRoles(){
        List<Rol> roles = rolService.obtenerRolOrdenPorId(); // Obtener todos los roles
        if (roles.isEmpty()) { // Si la lista esta vacía
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(roles); // Si hay roles, devuelve la lista de roles
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<?> obtenerRolPorId(@PathVariable Long id) {
        try {
            Rol rol = rolService.obtenerRolPorId(id);
            return ResponseEntity.ok(rol);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para consultar los usuarios
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = userService.obtenerUser();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> obtenerUserPorId(@PathVariable Long id) {
        try {
            User user = userService.obtenerUserPorId(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para crear un usuario nuevo
    @PostMapping("/users")
    public ResponseEntity<?> crearUser(@RequestBody User user){
        try {
            User nuevo = userService.crearUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getRol().getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para crear un rol nuevo
    @PostMapping("/roles")
    public ResponseEntity<?> crearRol(@RequestBody Rol role) {
        try {
            Rol nuevo = rolService.crearRol(role);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo); 
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    
    }

    // Endpoint para autenticar al usuario
    @PostMapping("/auth/login")
    public ResponseEntity<?> autenticar(@RequestParam String username, @RequestParam String password) {
        boolean autenticado = userService.autenticar(username, password);
        if (autenticado) {
            return ResponseEntity.ok("Autenticación exitosa");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }

    // Endpoint para actualizar a los usuarios
    @PutMapping("/users/{id}")
    public ResponseEntity<?> actualizarUser(@PathVariable Long id, @RequestParam(required = false) String username,@RequestParam(required = false) String email, @RequestParam(required = false) String password, @RequestParam(required = false) Long rolId) {
        try {
            User actualizado = userService.actualizarUser(id, username, email, password, rolId);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<?> actualizarRol(@PathVariable Long id, @RequestParam(required = false) String nombre) {
        try {
            Rol actualizado = rolService.actualizarRol(id, nombre);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para eliminar los roles
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<?> eliminarRol(@PathVariable Long id) {
        try {
            String mensaje = rolService.eliminarRol(id);
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para eliminar a los usuarios
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> eliminarUser(@PathVariable Long id) {
        try {
            String mensaje = userService.eliminarUser(id);
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}