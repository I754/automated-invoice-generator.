package com.example.invoice.service;

import com.example.invoice.dto.InvoiceRequest;
import com.example.invoice.model.Client;
import com.example.invoice.model.Invoice;
import com.example.invoice.model.LineItem;
import com.example.invoice.repo.ClientRepository;
import com.example.invoice.repo.InvoiceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepo;
    private final ClientRepository clientRepo;

    @Transactional
    public Invoice create(InvoiceRequest req) {
        Client client = clientRepo.findById(req.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Invoice inv = new Invoice();
        inv.setClient(client);
        inv.setDueDate(req.dueDate());
        if (req.taxPercent() != null) inv.setTaxPercent(req.taxPercent());

        // Add items
        inv.getItems().clear();
        req.items().forEach(it -> {
            LineItem li = new LineItem();
            li.setInvoice(inv);
            li.setDescription(it.description());
            li.setQuantity(it.quantity());
            li.setUnitPrice(it.unitPrice());
            li.setLineTotal(it.unitPrice().multiply(BigDecimal.valueOf(it.quantity())));
            inv.getItems().add(li);
        });

        // totals
        BigDecimal sub = inv.getItems().stream()
                .map(LineItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        inv.setSubTotal(sub);
        BigDecimal tax = sub.multiply(inv.getTaxPercent()).divide(BigDecimal.valueOf(100));
        inv.setTaxAmount(tax);
        inv.setTotalAmount(sub.add(tax));

        inv.setStatus(Invoice.Status.SENT);
        return invoiceRepo.save(inv);
    }
}
