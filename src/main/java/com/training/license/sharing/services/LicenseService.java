package com.training.license.sharing.services;

import com.training.license.sharing.dto.CredentialDTO;
import com.training.license.sharing.dto.ExpiringLicenseDTO;
import com.training.license.sharing.dto.LicenseDTO;
import com.training.license.sharing.dto.LicenseEditingDTO;
import com.training.license.sharing.dto.LicenseSummaryDTO;
import com.training.license.sharing.dto.NewLicenseDTO;
import com.training.license.sharing.dto.UnusedLicenseDTO;
import com.training.license.sharing.entities.Credential;
import com.training.license.sharing.entities.License;
import com.training.license.sharing.entities.LicenseCredential;
import com.training.license.sharing.entities.LicenseCredentialKey;
import com.training.license.sharing.entities.enums.DurationUnit;
import com.training.license.sharing.repositories.LicenseCredentialRepository;
import com.training.license.sharing.repositories.LicenseRepository;
import com.training.license.sharing.util.CredentialConverter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.training.license.sharing.util.LoggerUtil.logInfo;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LicenseService {

    private static final String DEFAULT_LOGO_BASE64 = "default-logo-base64-value";

    private final LicenseRepository licenseRepository;
    private final ModelMapper modelMapper;
    private final LicenseCredentialRepository licenseCredentialRepository;
    private final CredentialsService credentialsService;
    private final CredentialConverter credentialConverter;

    public List<ExpiringLicenseDTO> getActiveLicenses() {
        return licenseRepository.findAll().stream()
                .filter(this::isAvailable)
                .map(this::convertToExpiringLicenseDTO)
                .toList();
    }

    public DurationUnit determineDurationUnit(int days) {
        return days >= 365 ? DurationUnit.YEAR : DurationUnit.MONTH;
    }

    public List<UnusedLicenseDTO> getExpiredLicenses() {
        return licenseRepository.findAll().stream()
                .filter(license -> !isAvailable(license))
                .map(this::convertToUnusedLicenseDTO)
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

    public List<LicenseSummaryDTO> getAllLicenses(String name, String sortBy) {
        Stream<License> licenseStream = licenseRepository.findAll().stream();

        if (name != null && !name.isEmpty()) {
            licenseStream = licenseStream.filter(license ->
                    license.getLicenseName().toLowerCase().contains(name.toLowerCase()));
        }

        return licenseStream
                .sorted(Comparator.comparing(License::getCreatingDate))
                .map(this::convertToLicenseSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveNewLicense(NewLicenseDTO newLicense) {
        List<Credential> credentials = credentialsService.findByDTOs(newLicense.getCredentials());
        License savedLicense = licenseRepository.save(convertToLicense(newLicense));
        logInfo("Saving license: " + savedLicense.getLicenseName());
        relateLicenseWithCredentials(savedLicense, credentials);
    }

    public Optional<LicenseEditingDTO> getLicenseEditingDTOByName(String name) {
        return getLicenseByLicenseName(name)
                .map(this::getLicenseEditingDTO);
    }

    public Optional<License> getLicenseByLicenseName(String name) {
        return licenseRepository.findLicenseByLicenseName(name);
    }

    public boolean doesLicenseExistByNameExceptId(String name, Long id) {
        if (Objects.isNull(id)){
            id = 0L;
        }
        Optional<License> licenseOptional = licenseRepository.findLicenseByLicenseName(name);
        return licenseOptional.isPresent() && !Objects.equals(licenseOptional.get().getId(), id) ;
    }

    public boolean doesLicenseExistById(Long id){
        return licenseRepository.existsById(id);
    }

    @Transactional
    public void editLicense(LicenseEditingDTO licenseEditingDTO){
        License unupdatedLicense = licenseRepository.findById(licenseEditingDTO.getLicenseId()).get();
        License license = convertToLicense(licenseEditingDTO, unupdatedLicense);
        licenseRepository.save(license);
        updateLicenseCredentialRelations(license, licenseEditingDTO.getCredentials());
    }

    private void updateLicenseCredentialRelations(License license, List<CredentialDTO> credentialDTOList) {
        licenseCredentialRepository.deleteAllByLicenseId(license.getId());
        relateLicenseWithCredentials(license, credentialsService.findByDTOs(credentialDTOList));
    }

    private List<CredentialDTO> getCredentialDTOListByLicense(License license) {
        return licenseCredentialRepository.findAllByLicense(license).stream()
                .map(LicenseCredential::getCredential)
                .map(credentialConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    private LicenseEditingDTO getLicenseEditingDTO(License license) {
        return convertToLicenseEditingDTO(license, getCredentialDTOListByLicense(license));
    }

    private void relateLicenseWithCredentials(License license, List<Credential> credentials) {
        credentials.stream()
                .map(credential -> relateLicenseWithCredential(license, credential))
                .peek(licenseCredentialRepository::save)
                .forEach(LicenseService::logInfoAboutLicenseCredentialRelation);
    }

    private UnusedLicenseDTO convertToUnusedLicenseDTO(License license) {
        return modelMapper.map(license, UnusedLicenseDTO.class);
    }

    private ExpiringLicenseDTO convertToExpiringLicenseDTO(License license) {
        return modelMapper.map(license, ExpiringLicenseDTO.class);
    }

    private static void logInfoAboutLicenseCredentialRelation(LicenseCredential licenseCredential) {
        logInfo("Saving licenseCredentials relation between " +
                licenseCredential.getLicense().getLicenseName() +
                " and " + licenseCredential.getCredential().getUsername());
    }

    private LicenseCredentialKey generateLicenseCredentialKey(License license, Credential credential) {
        return new LicenseCredentialKey().toBuilder()
                .credentialId(credential.getId().intValue())
                .licenseId(license.getId().intValue())
                .build();
    }

    private LicenseCredential relateLicenseWithCredential(License license, Credential credential) {
        return new LicenseCredential().toBuilder()
                .credential(credential)
                .license(license)
                .id(generateLicenseCredentialKey(license, credential))
                .build();
    }

    private LicenseDTO convertToLicenseDTO(License license) {
        return modelMapper.map(license, LicenseDTO.class);
    }

    private LicenseEditingDTO convertToLicenseEditingDTO(License license, List<CredentialDTO> credentialDTOList) {
        return modelMapper.map(license, LicenseEditingDTO.class).toBuilder()
                .typeOfLicense(license.getLicenseType())
                .credentials(credentialDTOList)
                .build();
    }

    private License convertToLicense (LicenseEditingDTO licenseEditingDTO, License oldLicense){
        return modelMapper.map(licenseEditingDTO, License.class).toBuilder()
                .unusedPeriod(oldLicense.getUnusedPeriod())
                .creatingDate(oldLicense.getCreatingDate())
                .logo(convertLogoToBytes(licenseEditingDTO.getLogo()))
                .build();
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

    private LicenseSummaryDTO convertToLicenseSummaryDto(License license) {
        String logoBase64 = Optional.ofNullable(license.getLogo())
                .filter(logo -> logo.length > 0)
                .map(logo -> Base64.getEncoder().encodeToString(logo))
                .orElse(DEFAULT_LOGO_BASE64);

        return LicenseSummaryDTO.builder()
                .licenseName(license.getLicenseName())
                .description(license.getDescription())
                .cost(license.getCost())
                .currency(license.getCurrency())
                .licenseDuration(license.getUnusedPeriod())
                .durationUnit(determineDurationUnit(license.getUnusedPeriod()))
                .seatsAvailable(license.getSeatsAvailable())
                .seatsTotal(license.getSeatsTotal())
                .isActive(determineActiveStatus(license.getExpirationDate()))
                .expirationDate(license.getExpirationDate())
                .licenseType(license.getLicenseType())
                .isRecurring(license.getIsRecurring())
                .logo(Arrays.toString(logoBase64.getBytes()))
                .build();
    }

    private boolean determineActiveStatus(LocalDate expirationDate) {
        return LocalDate.now().isBefore(expirationDate);
    }

}
