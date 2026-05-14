package com.example.MarcketPlaceUniversitario.controller;

import com.example.MarcketPlaceUniversitario.DTO.PostImagenResponseDTO;
import com.example.MarcketPlaceUniversitario.model.PostImagenes;
import com.example.MarcketPlaceUniversitario.model.Posts;
import com.example.MarcketPlaceUniversitario.repository.PostRepository;
import com.example.MarcketPlaceUniversitario.service.CloudinaryService;
import com.example.MarcketPlaceUniversitario.service.PostImagenesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/postImagenes")
public class PostImagenesController {

    private final PostImagenesService postImagenesService;
    private final CloudinaryService cloudinaryService;
    private final PostRepository postRepository;

    public PostImagenesController(PostImagenesService postImagenesService,
                                  CloudinaryService cloudinaryService,
                                  PostRepository postRepository) {
        this.postImagenesService = postImagenesService;
        this.cloudinaryService = cloudinaryService;
        this.postRepository = postRepository;
    }

    /**
     * Sube una imagen a Cloudinary y la asocia al post indicado.
     * Multipart: campo "file" = imagen, campo "postId" = id del post
     */
    @PostMapping("/upload")
    public ResponseEntity<PostImagenResponseDTO> subirImagen(
            @RequestParam("file") MultipartFile file,
            @RequestParam("postId") Long postId) {

        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post no encontrado: " + postId));

        String url = cloudinaryService.subirImagen(file);

        PostImagenes imagen = new PostImagenes();
        imagen.setUrl(url);
        imagen.setOrden(0);
        imagen.setPosts(post);
        PostImagenes guardada = postImagenesService.save(imagen);

        PostImagenResponseDTO dto = new PostImagenResponseDTO(guardada.getId(), guardada.getUrl(), guardada.getOrden());
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public PostImagenes guardar(@RequestBody PostImagenes postImagenes) {
        return postImagenesService.save(postImagenes);
    }

    @GetMapping
    public List<PostImagenes> listar() {
        return postImagenesService.findAll();
    }

    @GetMapping("/{id}")
    public PostImagenes buscar(@PathVariable long id) {
        return postImagenesService.findById(id);
    }

    @PutMapping("/{id}")
    public PostImagenes actualizar(@PathVariable long id, @RequestBody PostImagenes postImagenes) {
        return postImagenesService.update(postImagenes, id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable long id) {
        postImagenesService.delete(id);
    }
}
