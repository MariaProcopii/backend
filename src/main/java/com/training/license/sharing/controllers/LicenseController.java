package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.LicenseDTO;
import com.training.license.sharing.services.LicenseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/license")
@AllArgsConstructor
public class LicenseController {

    private final LicenseService licenseService;

    @GetMapping("/get-expiring-licenses")
    public List<LicenseDTO> getExpiringLicenses() {
        return licenseService.getActiveLicenses();
    }

    @GetMapping("/get-unused-licenses")
    public List<LicenseDTO> getUnusedLicenses() {
        return licenseService.getExpiredLicenses();
    }
}
