package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.ExpiringLicenseDTO;
import com.training.license.sharing.dto.LicenseDTO;
import com.training.license.sharing.dto.UnusedLicenseDTO;
import com.training.license.sharing.services.LicenseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/license")
@AllArgsConstructor
public class LicenseController {

    private final LicenseService licenseService;
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-expiring-licenses")
    public List<ExpiringLicenseDTO> getExpiringLicenses() {
        return licenseService.getActiveLicenses();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-unused-licenses")
    public List<UnusedLicenseDTO> getUnusedLicenses() {
        return licenseService.getExpiredLicenses();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-new-license")
    public ResponseEntity addNewLicense(@RequestBody LicenseDTO licenseDTO){
        //TODO this functionality will be implemented insoon.
        return null;
    }

}
