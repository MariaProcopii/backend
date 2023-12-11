package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.CostViewDTO;
import com.training.license.sharing.services.CostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cost")
@RequiredArgsConstructor
public class CostController {

    private final CostService service;
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-cost")
    public ResponseEntity<CostViewDTO> getCost() {
        return ResponseEntity.ok(service.getCosts());
    }
}