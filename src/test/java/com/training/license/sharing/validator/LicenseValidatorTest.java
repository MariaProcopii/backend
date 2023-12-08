package com.training.license.sharing.validator;

import com.training.license.sharing.dto.LicenseEditingDTO;
import com.training.license.sharing.services.CredentialsService;
import com.training.license.sharing.services.LicenseService;
import com.training.license.sharing.util.CustomExceptions.ImageHasIllegalTypeException;
import com.training.license.sharing.util.CustomExceptions.ImageSizeOutOfBoundsException;
import com.training.license.sharing.util.CustomExceptions.LicenseExistentByIdException;
import com.training.license.sharing.util.CustomExceptions.LicenseExistentByNameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.training.license.sharing.util.LicenseTestData.BAD_LICENSE_NAME;
import static com.training.license.sharing.util.NewLicenseTestData.INCORRECT_SIZE_OF_IMAGE_NEW_LICENSE_DTO;
import static com.training.license.sharing.util.NewLicenseTestData.INCORRECT_TYPE_OF_IMAGE_NEW_LICENSE_DTO;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LicenseValidatorTest {

    @Spy
    private ImageValidator imageValidator;

    @Mock
    private CredentialsService credentialsService;

    @Mock
    private LicenseService licenseService;

    @InjectMocks
    private LicenseValidator licenseValidator;

    private LicenseEditingDTO licenseEditingDTOTest;

    @BeforeEach
    void setUp() {
        licenseEditingDTOTest = new LicenseEditingDTO().toBuilder()
                .licenseId(1L)
                .build();
    }

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

    @Test
    void getValidatedLicenseEditingDTOByNameShouldThrowException(){
        assertThatThrownBy(()->licenseValidator.getValidatedLicenseEditingDTOByName(BAD_LICENSE_NAME))
                .isInstanceOf(LicenseExistentByNameException.class);
    }

    @Test
    void validateEditingLicenseShouldThrowExceptionWithIncorrectId(){
        assertThatThrownBy(()->licenseValidator.validateEditingLicense(licenseEditingDTOTest))
                .isInstanceOf(LicenseExistentByIdException.class);
    }

    @Test
    void validateEditingLicenseShouldThrowExceptionWithIncorrectName(){
        when(licenseService.doesLicenseExistById(any())).thenReturn(true);
        when(licenseService.doesLicenseExistByNameExceptId(any(),any())).thenReturn(true);

        assertThatThrownBy(()->licenseValidator.validateEditingLicense(licenseEditingDTOTest))
                .isInstanceOf(LicenseExistentByNameException.class);
    }

}