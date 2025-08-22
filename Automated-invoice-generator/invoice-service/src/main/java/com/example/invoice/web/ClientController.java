package com.example.invoice.web;

import com.example.invoice.model.Client;
import com.example.invoice.repo.ClientRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClientController {
    private final ClientRepository repo;

    @GetMapping
    public List<Client> all(){ return repo.findAll(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client create(@RequestBody @Valid Client c){ return repo.save(c); }
}
