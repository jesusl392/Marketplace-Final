package com.example.MarcketPlaceUniversitario.service;

import com.example.MarcketPlaceUniversitario.DTO.DtoPrincipales.UsuarioRequestDTO;
import com.example.MarcketPlaceUniversitario.DTO.DtoPrincipales.UsuarioResponseDTO;
import com.example.MarcketPlaceUniversitario.model.Usuario;
import com.example.MarcketPlaceUniversitario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

    @Service
public class UsuarioServiceIMP implements UsuarioService{
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public Usuario findById(long id) {
        return  usuarioRepository.findById(id).orElseThrow(()->new RuntimeException("Usuario no encontrado"));
    }

        @Override
        public UsuarioResponseDTO save(UsuarioRequestDTO dto) {

            Usuario usuario = new Usuario();

            usuario.setNombre(dto.getNombre());

            usuario.setCorreo(dto.getCorreo());

            // BCrypt
            usuario.setPassword(
                    passwordEncoder.encode(dto.getPassword())
            );

            usuario.setRol("Usuario");

            usuario.setEstado(true);

            usuario.setFechaRegistro(LocalDate.now());

            Usuario guardado = usuarioRepository.save(usuario);

            return new UsuarioResponseDTO(
                    guardado.getId(),
                    guardado.getNombre(),
                    guardado.getCorreo(),
                    guardado.getRol(),
                    guardado.getEstado()
            );
        }

    @Override
    public Usuario update(Usuario usuario,long id) {
        Usuario existente=findById(id);
        existente.setNombre(usuario.getNombre());
        existente.setEstado(usuario.getEstado());
        existente.setCompras(usuario.getCompras());
        existente.setCorreo(usuario.getCorreo());
        existente.setFotoPerfil(usuario.getFotoPerfil());
        existente.setPassword(usuario.getPassword());
        return usuarioRepository.save(existente);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public void delete(long id) {
        findById(id);
        usuarioRepository.deleteById(id);
    }
}
