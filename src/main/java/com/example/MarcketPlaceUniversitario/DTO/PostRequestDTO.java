package com.example.MarcketPlaceUniversitario.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostRequestDTO {

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double precio;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    private String estado;

    private String ubicacion;

    @NotNull(message = "El id del usuario es obligatorio")
    private Long usuarioId;
}