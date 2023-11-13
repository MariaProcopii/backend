package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.LicenseDTO;
import com.training.license.sharing.services.LicenseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/license")
@AllArgsConstructor
public class LicenseController {

    private final LicenseService licenseService;

    @GetMapping("/get-expiring-licenses/{userId}")
    public List<LicenseDTO> getExpiringLicenses(@PathVariable Long userId) {
        return licenseService.getActiveLicenses(userId);
    }

    @GetMapping("/get-unused-licenses/{userId}")
    public List<LicenseDTO> getUnusedLicenses(@PathVariable Long userId) {
        return licenseService.getExpiredLicenses(userId);
    }
}
