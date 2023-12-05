package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.AverageUserCostResponseDTO;
import com.training.license.sharing.services.AverageUserCostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/average-user-cost")
@RequiredArgsConstructor
public class AverageUserCostController {

    private final AverageUserCostService service;
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-average-user-cost")
    public ResponseEntity<AverageUserCostResponseDTO> getAverageUserCost() {
        return ResponseEntity.ok(service.getAverageUserCosts());
    }
}