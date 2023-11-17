package com.training.license.sharing.services;

import com.training.license.sharing.dto.LicenseDTO;
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

    public List<LicenseDTO> getActiveLicenses() {
        return licenseRepository.findAll().stream()
                .filter(this::isAvailable)
                .map(this::convertToDTO)
                .toList();
    }

    public List<LicenseDTO> getExpiredLicenses() {
        return licenseRepository.findAll().stream()
                .filter(license -> !isAvailable(license))
                .map(this::convertToDTO)
                .toList();
    }

    public Optional<License> findByNameAndStartDate(String licenseName, LocalDate startOfUse) {
        return licenseRepository.findByLicenseName(licenseName, startOfUse)
                .stream()
                .findFirst();
    }

    public long findNumberOfUsersByLicense(License license) {
        return licenseRepository.findNumberOfUsersByLicense(license);
    }

    private LicenseDTO convertToDTO(License license) {
        return modelMapper.map(license, LicenseDTO.class);
    }

    private boolean isAvailable(License license) {
        return license.getAvailability() >= 0 && license.getUnusedPeriod() == 0;
    }

}
