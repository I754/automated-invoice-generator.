package com.example.invoice.web;

import com.example.invoice.dto.InvoiceRequest;
import com.example.invoice.model.Invoice;
import com.example.invoice.repo.InvoiceRepository;
import com.example.invoice.service.InvoiceService;
import com.example.invoice.util.PdfGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepo;

    @GetMapping
    public List<Invoice> all(){ return invoiceRepo.findAll(); }

    @PostMapping
    public Invoice create(@RequestBody InvoiceRequest req){ return invoiceService.create(req); }

    @GetMapping("/{id}")
    public Invoice get(@PathVariable Long id){ return invoiceRepo.findById(id).orElseThrow(); }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Long id){
        Invoice inv = invoiceRepo.findById(id).orElseThrow();
        byte[] bytes = PdfGenerator.generate(inv);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }
}
