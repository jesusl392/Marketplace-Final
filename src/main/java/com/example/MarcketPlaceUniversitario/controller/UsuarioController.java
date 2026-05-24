package com.example.MarcketPlaceUniversitario.controller;

import com.example.MarcketPlaceUniversitario.DTO.DtoPrincipales.UsuarioRequestDTO;
import com.example.MarcketPlaceUniversitario.DTO.DtoPrincipales.UsuarioResponseDTO;
import com.example.MarcketPlaceUniversitario.model.Usuario;
import com.example.MarcketPlaceUniversitario.repository.UsuarioRepository;
import com.example.MarcketPlaceUniversitario.service.CloudinaryService;
import com.example.MarcketPlaceUniversitario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/usuarios")
@RestController
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    CloudinaryService cloudinaryService;

    @PostMapping
    public UsuarioResponseDTO save(@Valid @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.save(dto);
    }

    @GetMapping
    public List<Usuario> findAll() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario findById(@PathVariable long id) {
        return usuarioService.findById(id);
    }

    @PutMapping("/{id}")
    public Usuario update(@PathVariable long id, @RequestBody Usuario usuario) {
        return usuarioService.update(usuario, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        usuarioService.delete(id);
    }

    /** Actualiza solo el nombre del usuario */
    @PatchMapping("/{id}/nombre")
    public ResponseEntity<String> actualizarNombre(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        u.setNombre(nombre);
        usuarioRepository.save(u);
        return ResponseEntity.ok(nombre);
    }

    /** Sube foto de perfil a Cloudinary y actualiza fotoPerfil del usuario */
    @PatchMapping("/{id}/foto")
    public ResponseEntity<String> actualizarFoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        String url = cloudinaryService.subirImagen(file);
        u.setFotoPerfil(url);
        usuarioRepository.save(u);
        return ResponseEntity.ok(url);
    }

    /** Elimina la foto de perfil (la pone en null) */
    @DeleteMapping("/{id}/foto")
    public ResponseEntity<Void> eliminarFoto(@PathVariable Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        u.setFotoPerfil(null);
        usuarioRepository.save(u);
        return ResponseEntity.noContent().build();
    }
}