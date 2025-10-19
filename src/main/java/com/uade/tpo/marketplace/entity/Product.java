package com.uade.tpo.marketplace.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne()
    @JoinColumn(name = "vendedor_id", nullable = false)
    private User seller;

    @OneToOne(mappedBy = "product")
    private Image image;

    @Column(nullable = false, columnDefinition = "double default 1.0")
    @DecimalMin(value = "0.0", inclusive = true, message = "El descuento debe ser mayor o igual a 0")
    @DecimalMax(value = "1.0", inclusive = true, message = "El descuento debe ser menor o igual a 1")
    @Builder.Default
    private Double discount = 1.0; // Multiplicador de precio (1.0 = sin descuento, 0.5 = 50% descuento)
}
