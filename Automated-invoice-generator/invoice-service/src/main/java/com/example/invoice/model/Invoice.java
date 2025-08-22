package com.example.invoice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter @Setter
public class Invoice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Client client;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineItem> items = new ArrayList<>();

    private LocalDate issueDate = LocalDate.now();
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT;

    private BigDecimal subTotal = BigDecimal.ZERO;
    private BigDecimal taxPercent = BigDecimal.valueOf(18);
    private BigDecimal taxAmount = BigDecimal.ZERO;
    private BigDecimal totalAmount = BigDecimal.ZERO;

    public enum Status { DRAFT, SENT, PAID, OVERDUE }
}
