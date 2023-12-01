package com.training.license.sharing.services;

import com.training.license.sharing.dto.ExpiringLicenseDTO;
import com.training.license.sharing.dto.UnusedLicenseDTO;
import com.training.license.sharing.entities.License;
import com.training.license.sharing.repositories.LicenseRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private final ModelMapper modelMapper;

    public List<ExpiringLicenseDTO> getActiveLicenses() {
        return licenseRepository.findAll().stream()
                .filter(this::isAvailable)
                .map(this::convertToExpiringLicenseDTO)
                .toList();
    }

    private ExpiringLicenseDTO convertToExpiringLicenseDTO(License license) {
        return modelMapper.map(license, ExpiringLicenseDTO.class);
    }

    public List<UnusedLicenseDTO> getExpiredLicenses() {
        return licenseRepository.findAll().stream()
                .filter(license -> !isAvailable(license))
                .map(this::convertToUnusedLicenseDTO)
                .toList();
    }

    private UnusedLicenseDTO convertToUnusedLicenseDTO(License license) {
        return modelMapper.map(license, UnusedLicenseDTO.class);
    }

    public Optional<License> findByNameAndStartDate(String licenseName, LocalDate startOfUse) {
        return licenseRepository.findByLicenseName(licenseName, startOfUse)
                .stream()
                .findFirst();
    }

    public long findNumberOfUsersByLicense(License license) {
        return licenseRepository.findNumberOfUsersByLicense(license);
    }

    private boolean isAvailable(License license) {
        if (license == null) {
            return false;
        }

        Integer availability = license.getAvailability();
        Integer unusedPeriod = license.getUnusedPeriod();

        if (availability == null || unusedPeriod == null) {
            return false;
        }

        return availability >= 0 && unusedPeriod == 0;
    }

}
