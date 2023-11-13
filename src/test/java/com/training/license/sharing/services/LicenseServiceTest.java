package com.training.license.sharing.services;

import com.training.license.sharing.dto.LicenseDTO;
import com.training.license.sharing.entities.License;
import com.training.license.sharing.entities.User;
import com.training.license.sharing.repositories.LicenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LicenseServiceTest {

    private static final String ACTIVE_LICENSE_NAME = "Active License";
    private static final String EXPIRED_LICENSE_NAME = "Active License";
    private static final int EXPECTED_ACTIVE_LICENSES_COUNT = 1;
    private static final int EXPECTED_EXPIRED_LICENSES_COUNT = 1;

    @Mock
    private LicenseRepository licenseRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private LicenseService licenseService;

    private User testUser;
    private License activeLicense;
    private License expiredLicense;

    @BeforeEach
    void setUp() {
        testUser = createUser();
        activeLicense = createActiveLicense(testUser);
        expiredLicense = createExpiredLicense(testUser);
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }

    private License createActiveLicense(User user) {
        return License.builder()
                .user(user)
                .licenseName(ACTIVE_LICENSE_NAME)
                .cost(100)
                .availability(10)
                .unusedPeriod(0)
                .build();
    }

    private License createExpiredLicense(User user) {
        return License.builder()
                .user(user)
                .licenseName(EXPIRED_LICENSE_NAME)
                .cost(50)
                .availability(0)
                .unusedPeriod(5)
                .build();
    }

    @Test
    void shouldGetActiveLicenses() {
        when(licenseRepository.findAll()).thenReturn(Arrays.asList(activeLicense, expiredLicense));

        final LicenseDTO activeLicenseDTO = new LicenseDTO();
        activeLicenseDTO.setName(ACTIVE_LICENSE_NAME);
        when(modelMapper.map(activeLicense, LicenseDTO.class)).thenReturn(activeLicenseDTO);

        final List<LicenseDTO> result = licenseService.getActiveLicenses(testUser.getId());

        assertEquals(EXPECTED_ACTIVE_LICENSES_COUNT, result.size());
        assertEquals(ACTIVE_LICENSE_NAME, result.get(0).getName());
        verify(licenseRepository).findAll();
        verify(modelMapper).map(activeLicense, LicenseDTO.class);
    }

    @Test
    void shouldGetExpiredLicenses() {
        when(licenseRepository.findAll()).thenReturn(Arrays.asList(activeLicense, expiredLicense));

        final LicenseDTO expiredLicenseDTO = new LicenseDTO();
        expiredLicenseDTO.setName(EXPIRED_LICENSE_NAME);
        when(modelMapper.map(expiredLicense, LicenseDTO.class)).thenReturn(expiredLicenseDTO);

        final List<LicenseDTO> result = licenseService.getExpiredLicenses(testUser.getId());

        assertEquals(EXPECTED_EXPIRED_LICENSES_COUNT, result.size());
        assertEquals(EXPIRED_LICENSE_NAME, result.get(0).getName());
        verify(licenseRepository).findAll();
        verify(modelMapper).map(expiredLicense, LicenseDTO.class);
    }
}
