package com.example.MarcketPlaceUniversitario.repository;

import com.example.MarcketPlaceUniversitario.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioIdAndPostId(Long usuarioId, Long postId);

    @Transactional
    void deleteByUsuarioIdAndPostId(Long usuarioId, Long postId);
}
