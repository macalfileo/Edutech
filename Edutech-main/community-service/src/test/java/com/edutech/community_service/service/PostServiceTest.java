package com.edutech.community_service.service;

import com.edutech.community_service.model.Post;
import com.edutech.community_service.repository.PostRepository;
import com.edutech.community_service.webclient.AuthClient;
import com.edutech.community_service.webclient.CourseClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private AuthClient authClient;

    @Mock
    private CourseClient courseClient;

    @InjectMocks
    private PostService postService;

    @Test
    void crearPost_valido() {
        Post post = new Post();
        post.setCourseId(10L);
        post.setUserId(20L);
        post.setContenido("¿Alguien entendió la actividad?");

        when(authClient.usuarioExiste(20L, "token123")).thenReturn(true);
        when(courseClient.cursoExiste(10L, "token123")).thenReturn(true);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post resultado = postService.crear(post, "token123");

        assertEquals("¿Alguien entendió la actividad?", resultado.getContenido());
        verify(postRepository).save(post);
    }

    @Test
    void crearPost_usuarioNoExiste() {
        Post post = new Post();
        post.setCourseId(10L);
        post.setUserId(99L);
        post.setContenido("Hola");

        when(authClient.usuarioExiste(99L, "token")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            postService.crear(post, "token");
        });

        assertEquals("El usuario no existe o no tiene permisos", ex.getMessage());
    }

    @Test
    void crearPost_cursoNoExiste() {
        Post post = new Post();
        post.setCourseId(777L);
        post.setUserId(88L);
        post.setContenido("Hola foro");

        when(authClient.usuarioExiste(88L, "token")).thenReturn(true);
        when(courseClient.cursoExiste(777L, "token")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            postService.crear(post, "token");
        });

        assertEquals("El curso no existe o no está disponible", ex.getMessage());
    }

    @Test
    void obtenerTodos_returnLista() {
        when(postRepository.findAll()).thenReturn(List.of(new Post(), new Post()));
        List<Post> lista = postService.obtenerTodos();
        assertEquals(2, lista.size());
    }

    @Test
    void obtenerPorCurso_returnLista() {
        when(postRepository.findByCourseIdOrderByFechaCreacionDesc(1L)).thenReturn(List.of(new Post()));
        List<Post> lista = postService.obtenerPorCurso(1L);
        assertEquals(1, lista.size());
    }

    @Test
    void obtenerPorUsuario_returnLista() {
        when(postRepository.findByUserIdOrderByFechaCreacionDesc(5L)).thenReturn(List.of(new Post(), new Post()));
        List<Post> lista = postService.obtenerPorUsuario(5L);
        assertEquals(2, lista.size());
    }

    @Test
    void eliminar_existe() {
        Post post = new Post();
        post.setId(10L);
        when(postRepository.findById(10L)).thenReturn(Optional.of(post));

        String mensaje = postService.eliminar(10L);

        assertEquals("Post eliminado correctamente", mensaje);
        verify(postRepository).delete(post);
    }

    @Test
    void eliminar_noExiste() {
        when(postRepository.findById(55L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            postService.eliminar(55L);
        });

        assertEquals("Post no encontrado. ID: 55", ex.getMessage());
    }
}
