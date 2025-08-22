package com.example.invoice.repo;

import com.example.invoice.model.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineItemRepository extends JpaRepository<LineItem, Long> {}
