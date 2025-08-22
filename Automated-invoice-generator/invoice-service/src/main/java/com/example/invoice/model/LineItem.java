package com.example.invoice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity @Getter @Setter
public class LineItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Invoice invoice;

    @NotBlank
    private String description;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal unitPrice;

    private BigDecimal lineTotal = BigDecimal.ZERO;
}
