package com.example.MarcketPlaceUniversitario.controller;

import com.example.MarcketPlaceUniversitario.DTO.FavoritoRequestDTO;
import com.example.MarcketPlaceUniversitario.DTO.PostResponseDTO;
import com.example.MarcketPlaceUniversitario.model.Favorito;
import com.example.MarcketPlaceUniversitario.repository.FavoritoRepository;
import com.example.MarcketPlaceUniversitario.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private PostsService postsService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<PostResponseDTO>> getFavoritos(@PathVariable Long userId) {
        List<Favorito> favoritos = favoritoRepository.findByUsuarioId(userId);
        List<PostResponseDTO> posts = favoritos.stream()
                .map(f -> {
                    try {
                        return postsService.findById(f.getPostId());
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<Void> addFavorito(@RequestBody FavoritoRequestDTO request) {
        if (!favoritoRepository.existsByUsuarioIdAndPostId(request.getUsuarioId(), request.getPostId())) {
            Favorito favorito = new Favorito();
            favorito.setUsuarioId(request.getUsuarioId());
            favorito.setPostId(request.getPostId());
            favoritoRepository.save(favorito);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/{postId}")
    public ResponseEntity<Void> removeFavorito(@PathVariable Long userId, @PathVariable Long postId) {
        favoritoRepository.deleteByUsuarioIdAndPostId(userId, postId);
        return ResponseEntity.ok().build();
    }
}
