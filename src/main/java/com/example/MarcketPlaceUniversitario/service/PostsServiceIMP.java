package com.example.MarcketPlaceUniversitario.service;

import com.example.MarcketPlaceUniversitario.DTO.PostImagenResponseDTO;
import com.example.MarcketPlaceUniversitario.DTO.PostRequestDTO;
import com.example.MarcketPlaceUniversitario.DTO.PostResponseDTO;
import com.example.MarcketPlaceUniversitario.model.Posts;
import com.example.MarcketPlaceUniversitario.model.Usuario;
import com.example.MarcketPlaceUniversitario.repository.PostRepository;
import com.example.MarcketPlaceUniversitario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostsServiceIMP implements PostsService {

    private final PostRepository postRepository;
    private final UsuarioRepository usuarioRepository;

    public PostsServiceIMP(PostRepository postRepository, UsuarioRepository usuarioRepository) {
        this.postRepository = postRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public PostResponseDTO save(PostRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Posts post = new Posts();
        post.setTitulo(dto.getTitulo());
        post.setDescricion(dto.getDescripcion());
        post.setPrecio(dto.getPrecio());
        post.setCategoria(dto.getCategoria());
        post.setEstado(dto.getEstado() != null ? dto.getEstado() : "activo");
        post.setUbicacion(dto.getUbicacion());
        post.setFechaPublicacion(LocalDate.now());
        post.setUsuario(usuario);

        return toDTO(postRepository.save(post));
    }

    @Override
    public PostResponseDTO update(PostRequestDTO dto, long id) {
        Posts existente = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));

        existente.setTitulo(dto.getTitulo());
        existente.setDescricion(dto.getDescripcion());
        existente.setPrecio(dto.getPrecio());
        existente.setCategoria(dto.getCategoria());
        if (dto.getEstado() != null) existente.setEstado(dto.getEstado());
        if (dto.getUbicacion() != null) existente.setUbicacion(dto.getUbicacion());

        return toDTO(postRepository.save(existente));
    }

    @Override
    public PostResponseDTO findById(long id) {
        Posts post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));
        return toDTO(post);
    }

    @Override
    public void delete(long id) {
        postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));
        postRepository.deleteById(id);
    }

    @Override
    public List<PostResponseDTO> findAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private PostResponseDTO toDTO(Posts post) {
        List<PostImagenResponseDTO> imagenes = post.getPostImagenes()
                .stream()
                .map(img -> new PostImagenResponseDTO(img.getId(), img.getUrl(), img.getOrden()))
                .collect(Collectors.toList());

        return new PostResponseDTO(
                post.getId(),
                post.getTitulo(),
                post.getDescricion(),
                post.getPrecio(),
                post.getCategoria(),
                post.getEstado(),
                post.getFechaPublicacion(),
                post.getUsuario().getId(),
                post.getUsuario().getNombre(),
                post.getUsuario().getFotoPerfil(),
                post.getUbicacion(),
                imagenes
        );
    }
}