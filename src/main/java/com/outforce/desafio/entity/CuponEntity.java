package com.outforce.desafio.entity;

import com.outforce.desafio.domain.enums.CuponStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Cupon")
public class CuponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 6)
    private String code;

    @Column(length = 800)
    private String description;

    @Column(length = 10)
    private double discountValue;

    @Column
    private LocalDateTime expirationDate;

    @Column
    private boolean published;

    @Column
    @Enumerated(EnumType.STRING)
    private CuponStatus status;

    @Column
    private boolean redeemed;

}
