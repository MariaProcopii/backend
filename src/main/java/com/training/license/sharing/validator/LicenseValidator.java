package com.training.license.sharing.validator;

import com.training.license.sharing.dto.CredentialDTO;
import com.training.license.sharing.dto.LicenseEditingDTO;
import com.training.license.sharing.dto.NewLicenseDTO;
import com.training.license.sharing.services.CredentialsService;
import com.training.license.sharing.services.LicenseService;
import com.training.license.sharing.util.CustomExceptions.CredentialsNonExistentException;
import com.training.license.sharing.util.CustomExceptions.LicenseExistentByIdException;
import com.training.license.sharing.util.CustomExceptions.LicenseExistentByNameException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.training.license.sharing.validator.ErrorMessagesUtil.LICENSE_WITH_ID_NON_EXISTENT_MESSAGE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.LICENSE_WITH_NAME_ALREADY_EXIST_MESSAGE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.LICENSE_WITH_NAME_NON_EXISTENT_MESSAGE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.USER_NOT_EXIST_FOR_CREDENTIAL;
import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
@Log4j2
public class LicenseValidator {


    private final ImageValidator imageValidator;
    private final CredentialsService credentialsService;
    private final LicenseService licenseService;

    public void validateNewLicense(NewLicenseDTO licenseDTO) {
        validateLicenseName(licenseDTO.getLicenseName(), null);
        validateImage(licenseDTO.getLogo());
        validateUserByCredentials(licenseDTO.getCredentials());
    }

    public void validateEditingLicense(LicenseEditingDTO licenseDTO) {
        validateLicenseId(licenseDTO.getLicenseId());
        validateLicenseName(licenseDTO.getLicenseName(), licenseDTO.getLicenseId());
        validateImage(licenseDTO.getLogo());
        validateUserByCredentials(licenseDTO.getCredentials());
    }

    private void validateLicenseId(Long licenseId) {
        if(!licenseService.doesLicenseExistById(licenseId)){
            log.error(LICENSE_WITH_ID_NON_EXISTENT_MESSAGE);
            throw new LicenseExistentByIdException(LICENSE_WITH_ID_NON_EXISTENT_MESSAGE);
        }
    }

    public LicenseEditingDTO getValidatedLicenseEditingDTOByName(String name){
        Optional<LicenseEditingDTO> licenseEditingDTOOptional = licenseService.getLicenseEditingDTOByName(name);

        if (licenseEditingDTOOptional.isEmpty()){
            log.error(LICENSE_WITH_NAME_NON_EXISTENT_MESSAGE);
            throw new LicenseExistentByNameException(LICENSE_WITH_NAME_NON_EXISTENT_MESSAGE);
        }
        return licenseEditingDTOOptional.get();
    }

    private void validateLicenseName(String licenseName, Long exceptedId) {
        if (licenseService.doesLicenseExistByNameExceptId(licenseName, exceptedId)){
            log.error(LICENSE_WITH_NAME_ALREADY_EXIST_MESSAGE);
            throw new LicenseExistentByNameException(LICENSE_WITH_NAME_ALREADY_EXIST_MESSAGE);
        }
    }

    private void validateImage(String logo) {
        if (nonNull(logo)) {
            imageValidator.logoImageTypeValidation(logo);
            imageValidator.logoImageSizeValidation(logo);
        }
    }

    private void validateUserByCredentials(List<CredentialDTO> credentialDTOS) {
        credentialDTOS.forEach(credentialDTO -> {
            if (!credentialsService.existsByUsernameAndPassword(credentialDTO.getUsername(), credentialDTO.getPassword())) {
                log.error(String.format(USER_NOT_EXIST_FOR_CREDENTIAL, credentialDTO.getUsername()));
                throw new CredentialsNonExistentException(String.format(USER_NOT_EXIST_FOR_CREDENTIAL, credentialDTO.getUsername()));
            }
        });
    }
}
