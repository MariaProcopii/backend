package com.training.license.sharing.services;

import com.training.license.sharing.dto.ExpiringLicenseDTO;
import com.training.license.sharing.dto.UnusedLicenseDTO;
import com.training.license.sharing.entities.License;
import com.training.license.sharing.repositories.LicenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LicenseServiceTest {

    private static final String ACTIVE_LICENSE_NAME = "Active License";
    private static final String EXPIRED_LICENSE_NAME = "Expired License";
    private static final int EXPECTED_ACTIVE_LICENSES_COUNT = 1;
    private static final int EXPECTED_EXPIRED_LICENSES_COUNT = 1;

    @Mock
    private LicenseRepository licenseRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private LicenseService licenseService;

    private License activeLicense;
    private License expiredLicense;

    @BeforeEach
    void setUp() {
        activeLicense = createActiveLicense();
        expiredLicense = createExpiredLicense();
    }

    private License createActiveLicense() {
        return License.builder()
                .licenseName(ACTIVE_LICENSE_NAME)
                .cost(100.0)
                .availability(180)
                .unusedPeriod(0)
                .expirationDate(LocalDate.of(2024, 4, 6))
                .build();
    }

    private License createExpiredLicense() {
        return License.builder()
                .licenseName(EXPIRED_LICENSE_NAME)
                .cost(50.0)
                .unusedPeriod(10)
                .availability(-1)
                .build();
    }

    @Test
    void shouldGetActiveLicenses() {
        when(licenseRepository.findAll()).thenReturn(Arrays.asList(activeLicense, expiredLicense));

        final ExpiringLicenseDTO activeLicenseDTO = new ExpiringLicenseDTO();
        activeLicenseDTO.setName(ACTIVE_LICENSE_NAME);
        when(modelMapper.map(activeLicense, ExpiringLicenseDTO.class)).thenReturn(activeLicenseDTO);

        final List<ExpiringLicenseDTO> result = licenseService.getActiveLicenses();

        assertThat(result).hasSize(EXPECTED_ACTIVE_LICENSES_COUNT);
        assertThat(result.get(0).getName()).isEqualTo(ACTIVE_LICENSE_NAME);
        verify(licenseRepository).findAll();
        verify(modelMapper).map(activeLicense, ExpiringLicenseDTO.class);
    }

    @Test
    void shouldGetExpiredLicenses() {
        when(licenseRepository.findAll()).thenReturn(Arrays.asList(activeLicense, expiredLicense));

        final UnusedLicenseDTO expiredLicenseDTO = new UnusedLicenseDTO();
        expiredLicenseDTO.setName(EXPIRED_LICENSE_NAME);
        when(modelMapper.map(expiredLicense, UnusedLicenseDTO.class)).thenReturn(expiredLicenseDTO);

        final List<UnusedLicenseDTO> result = licenseService.getExpiredLicenses();

        assertThat(result).hasSize(EXPECTED_EXPIRED_LICENSES_COUNT);
        assertThat(result.get(0).getName()).isEqualTo(EXPIRED_LICENSE_NAME);
        verify(licenseRepository).findAll();
        verify(modelMapper).map(expiredLicense, UnusedLicenseDTO.class);
    }
}
