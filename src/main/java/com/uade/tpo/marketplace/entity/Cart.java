package com.uade.tpo.marketplace.entity;

import java.time.OffsetDateTime;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Cart { 
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @UpdateTimestamp
    @Column(nullable = false)
    private OffsetDateTime dateTime;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart")
    private List<ItemCart> items ;
}
