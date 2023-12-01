package com.training.license.sharing.validator;

import com.training.license.sharing.dto.CredentialDTO;
import com.training.license.sharing.dto.NewLicenseDTO;
import com.training.license.sharing.services.CredentialsService;
import com.training.license.sharing.util.CustomExceptions.CredentialsNonExistentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class LicenseValidator {

    private final ImageValidator imageValidator;
    private final CredentialsService credentialsService;

    public void validateNewLicense(NewLicenseDTO licenseDTO) throws IOException {
        String logo = licenseDTO.getLogo();
        if (nonNull(logo)) {
            imageValidator.logoImageTypeValidation(logo);
            imageValidator.logoImageSizeValidation(logo);
        }
        validateUserByCredentials(licenseDTO.getCredentials());

    }

    private void validateUserByCredentials(List<CredentialDTO> credentialDTOS) {
        credentialDTOS.forEach(credentialDTO -> {
            if (!credentialsService.existsByUsernameAndPassword(credentialDTO.getUsername(), credentialDTO.getPassword())) {
                throw new CredentialsNonExistentException(String.format("User(%s) with this credentials does not exist", credentialDTO.getUsername()));
            }
        });
    }

}
