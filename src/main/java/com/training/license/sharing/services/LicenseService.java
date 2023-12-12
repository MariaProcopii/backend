package com.training.license.sharing.services;

import com.training.license.sharing.dto.CredentialDTO;
import com.training.license.sharing.dto.ExpiringLicenseDTO;
import com.training.license.sharing.dto.NewLicenseDTO;
import com.training.license.sharing.dto.LicenseSummaryDTO;
import com.training.license.sharing.dto.UnusedLicenseDTO;
import com.training.license.sharing.entities.Credential;
import com.training.license.sharing.entities.License;
import com.training.license.sharing.entities.enums.DurationUnit;
import com.training.license.sharing.entities.LicenseCredential;
import com.training.license.sharing.entities.LicenseCredentialKey;
import com.training.license.sharing.repositories.LicenseCredentialRepository;
import com.training.license.sharing.repositories.LicenseRepository;
import com.training.license.sharing.util.CredentialConverter;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

import static com.training.license.sharing.util.InfoMessageUtil.CONVERT_LOGO_TO_BYTES;
import static com.training.license.sharing.util.InfoMessageUtil.DETERMINE_ACTIVE_STATUS;
import static com.training.license.sharing.util.InfoMessageUtil.DETERMINE_DURATION_UNIT;
import static com.training.license.sharing.util.InfoMessageUtil.EDIT_LICENSE;
import static com.training.license.sharing.util.InfoMessageUtil.FIND_BY_NAME_AND_START_DATE;
import static com.training.license.sharing.util.InfoMessageUtil.FIND_NUMBER_OF_USERS_BY_LICENSE;
import static com.training.license.sharing.util.InfoMessageUtil.GENERATE_LICENSE_CREDENTIAL_KEY;
import static com.training.license.sharing.util.InfoMessageUtil.GET_ACTIVE_LICENSES;
import static com.training.license.sharing.util.InfoMessageUtil.GET_ALL_LICENSES;
import static com.training.license.sharing.util.InfoMessageUtil.GET_EXPIRED_LICENSES;
import static com.training.license.sharing.util.InfoMessageUtil.GET_LICENSE_BY_NAME;
import static com.training.license.sharing.util.InfoMessageUtil.IS_AVAILABLE;
import static com.training.license.sharing.util.InfoMessageUtil.LICENSE_CREDENTIAL_RELATION;
import static com.training.license.sharing.util.InfoMessageUtil.RELATE_LICENSE_WITH_CREDENTIAL;
import static com.training.license.sharing.util.InfoMessageUtil.RELATE_LICENSE_WITH_CREDENTIALS;
import static com.training.license.sharing.util.InfoMessageUtil.SAVE_NEW_LICENSE;

@Service
@AllArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class LicenseService {

    private static final String DEFAULT_LOGO_BASE64 = "default-logo-base64-value";

    private final LicenseRepository licenseRepository;
    private final ModelMapper modelMapper;
    private final LicenseCredentialRepository licenseCredentialRepository;
    private final CredentialsService credentialsService;
    private final CredentialConverter credentialConverter;

    public List<ExpiringLicenseDTO> getActiveLicenses() {
        log.info(GET_ACTIVE_LICENSES);
        return licenseRepository.findAll().stream()
                .filter(this::isAvailable)
                .map(this::convertToExpiringLicenseDTO)
                .toList();
    }


    public List<UnusedLicenseDTO> getExpiredLicenses() {
        log.info(GET_EXPIRED_LICENSES);
        return licenseRepository.findAll().stream()
                .filter(license -> !isAvailable(license))
                .map(this::convertToUnusedLicenseDTO)
                .toList();
    }

    public Optional<License> findByNameAndStartDate(String licenseName, LocalDate startOfUse) {
        log.info(FIND_BY_NAME_AND_START_DATE, licenseName, startOfUse);
        return licenseRepository.findByLicenseName(licenseName, startOfUse)
                .stream()
                .sorted(Comparator.comparing(License::getActivationDate).reversed())
                .findFirst();
    }


    public long findNumberOfUsersByLicense(License license) {
        log.info(FIND_NUMBER_OF_USERS_BY_LICENSE, license.getLicenseName());
        return licenseRepository.findNumberOfUsersByLicense(license);
    }

    @Transactional
    public Long saveNewLicense(NewLicenseDTO newLicense) {
        List<Credential> credentials = credentialsService.findByDTOs(newLicense.getCredentials());
        License savedLicense = licenseRepository.save(convertToLicense(newLicense));
        log.info(SAVE_NEW_LICENSE, newLicense.getLicenseName());
        relateLicenseWithCredentials(savedLicense, credentials);
        return savedLicense.getId();
    }

    public Optional<NewLicenseDTO> getLicenseEditingDTOByName(String name) {
        return getLicenseByLicenseName(name)
                .map(this::getLicenseEditingDTO);
    }

    public Optional<License> getLicenseByLicenseName(String name) {
        log.info(GET_LICENSE_BY_NAME, name);
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
        return id != null && licenseRepository.existsById(id);
    }

    @Transactional
    public void editLicense(NewLicenseDTO newLicenseDTO){
        log.info(EDIT_LICENSE, newLicenseDTO.getLicenseId());
        License unupdatedLicense = licenseRepository.findById(newLicenseDTO.getLicenseId()).get();
        License license = convertToLicense(newLicenseDTO, unupdatedLicense);
        licenseRepository.save(license);
        updateLicenseCredentialRelations(license, newLicenseDTO.getCredentials());
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

    private NewLicenseDTO getLicenseEditingDTO(License license) {
        return convertToLicenseEditingDTO(license, getCredentialDTOListByLicense(license));
    }

    private void relateLicenseWithCredentials(License license, List<Credential> credentials) {
        log.info(RELATE_LICENSE_WITH_CREDENTIALS);
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
        log.info(LICENSE_CREDENTIAL_RELATION,
                licenseCredential.getLicense().getLicenseName(),
                licenseCredential.getCredential().getUsername()
        );
    }

    private LicenseCredentialKey generateLicenseCredentialKey(License license, Credential credential) {
        log.info(GENERATE_LICENSE_CREDENTIAL_KEY);
        return new LicenseCredentialKey().toBuilder()
                .credentialId(credential.getId().intValue())
                .licenseId(license.getId().intValue())
                .build();
    }

    private LicenseCredential relateLicenseWithCredential(License license, Credential credential) {
        log.info(RELATE_LICENSE_WITH_CREDENTIAL);
        return new LicenseCredential().toBuilder()
                .credential(credential)
                .license(license)
                .id(generateLicenseCredentialKey(license, credential))
                .build();
    }

    private NewLicenseDTO convertToLicenseEditingDTO(License license, List<CredentialDTO> credentialDTOList) {
        return modelMapper.map(license, NewLicenseDTO.class).toBuilder()
                .typeOfLicense(license.getLicenseType())
                .credentials(credentialDTOList)
                .build();
    }

    private License convertToLicense (NewLicenseDTO newLicenseDTO, License oldLicense){
        return modelMapper.map(newLicenseDTO, License.class).toBuilder()
                .unusedPeriod(oldLicense.getUnusedPeriod())
                .creatingDate(oldLicense.getCreatingDate())
                .logo(convertLogoToBytes(newLicenseDTO.getLogo()))
                .build();
    }

    private License convertToLicense(NewLicenseDTO licenseDTO) {
        return modelMapper.map(licenseDTO, License.class).toBuilder()
                .id(null)
                .seatsAvailable(evaluateAvailableSeats(licenseDTO))
                .logo(convertLogoToBytes(licenseDTO.getLogo()))
                .seatsTotal(licenseDTO.getSeatsTotal())
                .creatingDate(LocalDate.now())
                .unusedPeriod(0)
                .build();
    }

    private int evaluateAvailableSeats(NewLicenseDTO licenseDTO) {
        return licenseDTO.getSeatsTotal() - licenseDTO.getCredentials().size();
    }

    private byte[] convertLogoToBytes(String logo) {
        log.info(CONVERT_LOGO_TO_BYTES);
        return Objects.nonNull(logo) ? logo.getBytes() : null;
    }

    private boolean isAvailable(License license) {
        log.info(IS_AVAILABLE, license.getId());
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

    public DurationUnit determineDurationUnit(int days) {
        log.info(DETERMINE_DURATION_UNIT, days);
        return days >= 365 ? DurationUnit.YEAR : DurationUnit.MONTH;
    }

    private boolean determineActiveStatus(LocalDate expirationDate) {
        log.info(DETERMINE_ACTIVE_STATUS, expirationDate);
        return LocalDate.now().isBefore(expirationDate);
    }

    public List<LicenseSummaryDTO> getAllLicenses(String name, String sortBy) {
        log.info(GET_ALL_LICENSES, name);
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

}
