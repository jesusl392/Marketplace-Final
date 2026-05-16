    package com.example.MarcketPlaceUniversitario.model;

    import com.fasterxml.jackson.annotation.JsonBackReference;
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
    public class Posts {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(nullable = false)
        private String titulo;
        @Column(nullable = false)
        private String descricion;
        @Column(nullable = false)
        private double precio;
        @Column(nullable = false)
        private String categoria;
        @Column(nullable = false)
        private String estado; //activo,vendido,eliminado
        @Column
        private LocalDate fechaPublicacion;
        @Column
        private String ubicacion;



        @ManyToOne (fetch = FetchType.EAGER)
        @JoinColumn (name = "IdUsuario",nullable = false)
        @JsonBackReference ("Usuario-Posts")
        private Usuario usuario;

        @OneToMany (mappedBy = "posts",cascade = CascadeType.ALL)
        @JsonManagedReference
        private List<PostImagenes> postImagenes=new ArrayList<>();

        @OneToMany (mappedBy = "posts",cascade = CascadeType.ALL)
        @JsonManagedReference
        private List<PurchaseRequest> purchaseRequests=new ArrayList<>();




    }
