package com.example.MarcketPlaceUniversitario.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column  (nullable = false,length = 100)
    private String nombre;
    @Column  (nullable = false,unique = true,length = 150)
    private String correo;
    @Column (nullable = false)
    private String password;
    @Column (nullable = false,length = 20)
    private String rol;
    @Column
    private String fotoPerfil;
    @Column (nullable = false)
    private Boolean estado;
    @Column (nullable = false)
    private LocalDate fechaRegistro;
    // Acceso a la app de administrador — se cambia manualmente en la BD (por defecto false)
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean admin = false;

    @OneToMany(mappedBy = "usuario",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Posts> posts=new ArrayList<>();

    @OneToMany(mappedBy = "buyer",cascade = CascadeType.ALL)
    private List<PurchaseRequest> compras;

    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
    private List<PurchaseRequest> ventas;

    @OneToMany(mappedBy = "receiver",cascade = CascadeType.ALL)
    private List<Message> receivers;

    @OneToMany(mappedBy = "sender",cascade = CascadeType.ALL)
    private List<Message> senders;

    @OneToMany (mappedBy = "usuario",cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany (mappedBy = "from",cascade = CascadeType.ALL)
    private List<Ratings> froms;

    @OneToMany (mappedBy = "to",cascade = CascadeType.ALL)
    private List<Ratings> tos;


}
