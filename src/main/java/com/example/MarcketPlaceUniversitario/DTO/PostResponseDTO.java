package com.example.MarcketPlaceUniversitario.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private Double precio;
    private String categoria;
    private String estado;
    private LocalDate fechaPublicacion;
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioFoto;
    private String ubicacion;
    private List<PostImagenResponseDTO> imagenes;
}