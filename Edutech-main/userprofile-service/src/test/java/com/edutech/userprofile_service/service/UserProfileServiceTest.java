package com.edutech.userprofile_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.edutech.userprofile_service.model.UserProfile;
import com.edutech.userprofile_service.repository.UserProfileRepository;
import com.edutech.userprofile_service.webclient.AuthClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private UserProfileService userProfileService;

    @Test // Caso: userId es null
    void crearPerfil_userIdNull_lanzaExcepcion() {
        UserProfile perfil = new UserProfile();
        perfil.setUserId(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userProfileService.crearPerfil(perfil);
        });

        assertEquals("El ID del usuario (userId) es obligatorio", ex.getMessage());
    }

    @Test // Caso: usuario no existe
    void crearPerfil_usuarioNoExiste_lanzaExcepcion() {
        UserProfile perfil = new UserProfile();
        perfil.setUserId(99L);

        when(authClient.existeUsuario(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userProfileService.crearPerfil(perfil);
        });

        assertTrue(ex.getMessage().contains("El usuario con ID"));
    }

    @Test // Caso: usuario válido
    void crearPerfil_usuarioValido_guardaPerfil() {
        UserProfile perfil = new UserProfile();
        perfil.setUserId(1L);
        perfil.setTelefono("987654321");

        when(authClient.existeUsuario(1L)).thenReturn(true);
        when(userProfileRepository.save(perfil)).thenReturn(perfil);

        UserProfile resultado = userProfileService.crearPerfil(perfil);

        assertEquals(perfil, resultado);
        verify(userProfileRepository, times(1)).save(perfil);
    }


    @Test // Caso: obtener perfil por ID existente
    void obtenerPerfil_porIdExiste_devuelvePerfil() {
        UserProfile perfil = new UserProfile();
        perfil.setId(10L);
        perfil.setUserId(1L);

        when(userProfileRepository.findById(10L)).thenReturn(Optional.of(perfil));

        UserProfile resultado = userProfileService.obtenerUserProfilePorId(10L);

        assertNotNull(resultado);
        assertEquals(perfil, resultado);
    }

    @Test // Caso: obtener perfil por ID inexistente
    void obtenerPerfil_porIdNoExiste_lanzaExcepcion() {
        Long idNoExistente = 99L;
        when(userProfileRepository.findById(idNoExistente)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userProfileService.obtenerUserProfilePorId(idNoExistente);
        });

        assertEquals("Perfil de usuario no encontrado Id: " + idNoExistente, ex.getMessage());
    }


    @Test // Caso: eliminar perfil existente
    void eliminarPerfil_existente_ejecucionExitosa() {
        Long id = 5L;
       UserProfile perfil = new UserProfile();
        perfil.setId(id);

        when(userProfileRepository.findById(id)).thenReturn(Optional.of(perfil));
        doNothing().when(userProfileRepository).delete(perfil);

        assertDoesNotThrow(() -> userProfileService.eliminarUserProfile(id));
        verify(userProfileRepository, times(1)).delete(perfil);
    }
}