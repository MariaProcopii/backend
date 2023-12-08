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
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.training.license.sharing.validator.ErrorMessagesUtil.LICENSE_WITH_ID_NON_EXISTENT_MESSAGE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.LICENSE_WITH_NAME_ALREADY_EXIST_MESSAGE;
import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
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
        if(!licenseService.doesLicenseExistById(licenseId))
            throw new LicenseExistentByIdException(LICENSE_WITH_ID_NON_EXISTENT_MESSAGE);
    }

    public LicenseEditingDTO getValidatedLicenseEditingDTOByName(String name){
        Optional<LicenseEditingDTO> licenseEditingDTOOptional = licenseService.getLicenseEditingDTOByName(name);

        if (licenseEditingDTOOptional.isEmpty())
            throw new LicenseExistentByNameException(ErrorMessagesUtil.LICENSE_WITH_NAME_NON_EXISTENT_MESSAGE);

        return licenseEditingDTOOptional.get();
    }

    private void validateLicenseName(String licenseName, Long exceptedId) {
        if (licenseService.doesLicenseExistByNameExceptId(licenseName, exceptedId))
            throw new LicenseExistentByNameException(LICENSE_WITH_NAME_ALREADY_EXIST_MESSAGE);
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
                throw new CredentialsNonExistentException(String.format("User(%s) with this credentials does not exist", credentialDTO.getUsername()));
            }
        });
    }

}
