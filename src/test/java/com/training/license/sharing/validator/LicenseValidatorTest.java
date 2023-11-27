package com.training.license.sharing.validator;

import com.training.license.sharing.services.CredentialsService;
import com.training.license.sharing.util.CustomExceptions.ImageHasIllegalTypeException;
import com.training.license.sharing.util.CustomExceptions.ImageSizeOutOfBoundsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.training.license.sharing.util.NewLicenseTestData.INCORRECT_SIZE_OF_IMAGE_NEW_LICENSE_DTO;
import static com.training.license.sharing.util.NewLicenseTestData.INCORRECT_TYPE_OF_IMAGE_NEW_LICENSE_DTO;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class LicenseValidatorTest {

    @Spy
    private ImageValidator imageValidator;

    @Mock
    private CredentialsService credentialsService;

    @InjectMocks
    private LicenseValidator licenseValidator;

    @Test
    void validateNewLicenseShouldThrowExceptionWithInvalidImageType(){
        assertThatThrownBy(()->licenseValidator.validateNewLicense(INCORRECT_TYPE_OF_IMAGE_NEW_LICENSE_DTO))
                .isInstanceOf(ImageHasIllegalTypeException.class);
    }

    @Test
    void validateNewLicenseShouldThrowExceptionWithInvalidImageSize(){
        assertThatThrownBy(()->licenseValidator.validateNewLicense(INCORRECT_SIZE_OF_IMAGE_NEW_LICENSE_DTO))
                .isInstanceOf(ImageSizeOutOfBoundsException.class);
    }

}