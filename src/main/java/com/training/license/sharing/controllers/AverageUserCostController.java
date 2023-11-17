package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.AverageUserCostViewDTO;
import com.training.license.sharing.services.AverageUserCostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/average-user-cost")
@RequiredArgsConstructor
public class AverageUserCostController {

    private final AverageUserCostService service;

    @GetMapping("/get-average-user-cost")
    public ResponseEntity<List<AverageUserCostViewDTO>> getAverageUserCost() {
        return ResponseEntity.ok(service.getAverageUserCosts());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}