package com.example.invoice.config;

import com.example.invoice.model.Client;
import com.example.invoice.repo.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration @RequiredArgsConstructor
public class DemoData {
    @Bean
    CommandLineRunner seed(ClientRepository clients){
        return args -> {
            if (clients.count() == 0){
                Client c = new Client();
                c.setName("Acme Corp");
                c.setEmail("billing@acme.example");
                c.setAddress("221B Baker Street, London");
                c.setGstNumber("ACMEGST1234Z5");
                clients.save(c);
            }
        };
    }
}
