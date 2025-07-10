package com.edutech.community_service.service;

import com.edutech.community_service.model.Post;
import com.edutech.community_service.repository.PostRepository;
import com.edutech.community_service.webclient.AuthClient;
import com.edutech.community_service.webclient.CourseClient;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private CourseClient courseClient;

    // Crear post
    public Post crear(Post post, String token) {
        if (post.getUserId() == null || post.getCourseId() == null || post.getContenido() == null) {
            throw new RuntimeException("Faltan campos obligatorios: userId, courseId o contenido");
        }

        if (!authClient.usuarioExiste(post.getUserId(), token)) {
            throw new RuntimeException("El usuario no existe o no tiene permisos");
        }

        if (!courseClient.cursoExiste(post.getCourseId(), token)) {
            throw new RuntimeException("El curso no existe o no está disponible");
        }

        return postRepository.save(post);
    }

    // Obtener todos los posts
    public List<Post> obtenerTodos() {
        return postRepository.findAll();
    }

    // Obtener por curso
    public List<Post> obtenerPorCurso(Long cursoId) {
        return postRepository.findByCourseIdOrderByFechaCreacionDesc(cursoId);
    }

    // Obtener por usuario
    public List<Post> obtenerPorUsuario(Long userId) {
        return postRepository.findByUserIdOrderByFechaCreacionDesc(userId);
    }

    // Eliminar post
    public String eliminar(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado. ID: " + id));

        postRepository.delete(post);
        return "Post eliminado correctamente";
    }
}
