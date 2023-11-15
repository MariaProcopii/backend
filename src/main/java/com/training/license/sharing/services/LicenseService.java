package com.training.license.sharing.services;


import com.training.license.sharing.dto.LicenseDTO;
import com.training.license.sharing.entities.License;
import com.training.license.sharing.repositories.LicenseRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;



@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private final ModelMapper modelMapper;


    public List<LicenseDTO> getActiveLicenses(Long userId) {
        return licenseRepository.findAll().stream()
                .filter(license -> isAvailable(license) && getUserId(license).equals(userId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LicenseDTO> getExpiredLicenses(Long userId) {
        return licenseRepository.findAll().stream()
                .filter(license -> !isAvailable(license) && getUserId(license).equals(userId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LicenseDTO convertToDTO(License license) {
        return modelMapper.map(license, LicenseDTO.class);
    }

    private Long getUserId(License license) {
        return license.getUser() != null ? license.getUser().getId() : null;
    }

    private boolean isAvailable(License license) {
        return license.getAvailability() >= 0 && license.getUnusedPeriod() == 0;
    }
}
