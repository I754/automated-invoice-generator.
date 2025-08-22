package com.example.invoice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record InvoiceRequest(
        Long clientId,
        LocalDate dueDate,
        BigDecimal taxPercent,
        List<Item> items
) {
    public record Item(String description, Integer quantity, BigDecimal unitPrice) {}
}
