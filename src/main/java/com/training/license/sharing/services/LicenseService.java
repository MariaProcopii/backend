package com.training.license.sharing.services;

import com.training.license.sharing.dto.ExpiringLicenseDTO;
import com.training.license.sharing.dto.UnusedLicenseDTO;
import com.training.license.sharing.dto.LicenseDTO;
import com.training.license.sharing.dto.NewLicenseDTO;
import com.training.license.sharing.entities.Credential;
import com.training.license.sharing.entities.License;
import com.training.license.sharing.entities.LicenseCredential;
import com.training.license.sharing.entities.LicenseCredentialKey;
import com.training.license.sharing.repositories.LicenseCredentialRepository;
import com.training.license.sharing.repositories.LicenseRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.training.license.sharing.util.LoggerUtil.logInfo;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private final ModelMapper modelMapper;
    private final LicenseCredentialRepository licenseCredentialRepository;
    private final CredentialsService credentialsService;

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

    @Transactional
    public void saveNewLicense(NewLicenseDTO newLicense) {
        List<Credential> credentials = credentialsService.findByDTOs(newLicense.getCredentials());
        License savedLicense = licenseRepository.save(convertToLicense(newLicense));
        logInfo("Saving license: " + savedLicense.getLicenseName());
        relateLicenseWithCredentials(savedLicense, credentials);
    }

    private void relateLicenseWithCredentials(License license, List<Credential> credentials) {
        credentials.stream()
                .map(credential -> relateLicenseWithCredential(license, credential))
                .peek(licenseCredentialRepository::save)
                .forEach(LicenseService::logInfoAboutLicenseCredentialRelation);
    }

    private static void logInfoAboutLicenseCredentialRelation(LicenseCredential licenseCredential) {
        logInfo("Saving licenseCredentials relation between " +
                licenseCredential.getLicense().getLicenseName() +
                " and " + licenseCredential.getCredential().getUsername());
    }

    private LicenseCredential relateLicenseWithCredential(License license, Credential credential) {
        return new LicenseCredential().toBuilder()
                .credential(credential)
                .license(license)
                .id(generateLicenseCredentialKey(license, credential))
                .build();
    }

    private LicenseCredentialKey generateLicenseCredentialKey(License license, Credential credential) {
        return new LicenseCredentialKey().toBuilder()
                .credentialId(credential.getId().intValue())
                .licenseId(license.getId().intValue())
                .build();
    }

    private LicenseDTO convertToDTO(License license) {
        return modelMapper.map(license, LicenseDTO.class);
    }

    private License convertToLicense(NewLicenseDTO licenseDTO) {
        return modelMapper.map(licenseDTO, License.class).toBuilder()
                .seatsAvailable(evaluateAvailableSeats(licenseDTO))
                .logo(convertLogoToBytes(licenseDTO.getLogo()))
                .seatsTotal(licenseDTO.getSeats())
                .creatingDate(LocalDate.now())
                .unusedPeriod(0)
                .build();
    }

    private int evaluateAvailableSeats(NewLicenseDTO licenseDTO) {
        return licenseDTO.getSeats() - licenseDTO.getCredentials().size();
    }

    private byte[] convertLogoToBytes(String logo) {
        return Objects.nonNull(logo) ? logo.getBytes() : null;
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
