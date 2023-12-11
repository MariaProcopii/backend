package com.training.license.sharing.services;

import com.training.license.sharing.dto.ExpiringLicenseDTO;
import com.training.license.sharing.dto.NewLicenseDTO;
import com.training.license.sharing.dto.LicenseSummaryDTO;
import com.training.license.sharing.dto.UnusedLicenseDTO;
import com.training.license.sharing.entities.License;
import com.training.license.sharing.entities.enums.Currency;
import com.training.license.sharing.entities.enums.LicenseType;
import com.training.license.sharing.repositories.LicenseCredentialRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.training.license.sharing.util.NewLicenseTestData.CORRECT_NEW_LICENSE;
import static com.training.license.sharing.util.NewLicenseTestData.CORRECT_NEW_LICENSE_DTO;
import static com.training.license.sharing.util.NewLicenseTestData.TEST_CREDENTIALS_LIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LicenseServiceTest {

    private static final String ACTIVE_LICENSE_NAME = "Active License";
    private static final String EXPIRED_LICENSE_NAME = "Expired License";
    private static final String CREATING_DATE = "creatingDate";
    private static final String LICENSE_A = "License A";
    private static final String LICENSE_B = "License B";
    private static final String DESCRIPTION_A = "Description A";
    private static final String DESCRIPTION_B = "Description B";

    private static final int EXPECTED_ACTIVE_LICENSES_COUNT = 1;
    private static final int EXPECTED_EXPIRED_LICENSES_COUNT = 1;

    @Mock
    private LicenseRepository licenseRepository;

    @Mock
    private LicenseCredentialRepository licenseCredentialRepository;

    @Mock
    private CredentialsService credentialsService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private LicenseService licenseService;

    private License activeLicense;
    private License expiredLicense;
    private NewLicenseDTO newLicenseDTOTest;

    @BeforeEach
    void setUp() {
        activeLicense = createActiveLicense();
        expiredLicense = createExpiredLicense();
        newLicenseDTOTest = new NewLicenseDTO().toBuilder()
                .licenseId(1L)
                .build();
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

    @Test
    public void testGetAllLicensesWhenWeHaveNoLicenses() {
        when(licenseRepository.findAll()).thenReturn(Collections.emptyList());

        final List<LicenseSummaryDTO> result = licenseService.getAllLicenses(null, CREATING_DATE);

        assertThat(result).isEmpty();
        verify(licenseRepository).findAll();
    }

    @Test
    void shouldReturnFilteredLicenses() {
        final License licenseA = createTestLicenseA();
        final License licenseB = createTestLicenseB();

        when(licenseRepository.findAll()).thenReturn(Arrays.asList(licenseA, licenseB));

        String filterName = "A";
        List<LicenseSummaryDTO> result = licenseService.getAllLicenses(filterName, CREATING_DATE);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLicenseName()).isEqualTo(LICENSE_A);
    }

    private License createTestLicenseA() {
        return License.builder()
                .licenseName(LICENSE_A)
                .creatingDate(LocalDate.now())
                .cost(100.0)
                .availability(10)
                .licenseType(LicenseType.SOFTWARE)
                .unusedPeriod(30)
                .activationDate(LocalDate.now().minusDays(10))
                .expirationDate(LocalDate.now().plusDays(30))
                .logo(new byte[]{})
                .website("http://exampleA.com")
                .description(DESCRIPTION_A)
                .currency(Currency.USD)
                .seatsAvailable(5)
                .seatsTotal(10)
                .isRecurring(true)
                .build();
    }

    private License createTestLicenseB() {
        return License.builder()
                .licenseName(LICENSE_B)
                .creatingDate(LocalDate.now().minusDays(1))
                .cost(200.0)
                .availability(5)
                .licenseType(LicenseType.TRAINING)
                .unusedPeriod(60)
                .activationDate(LocalDate.now().minusDays(20))
                .expirationDate(LocalDate.now().plusDays(60))
                .logo(new byte[]{})
                .website("http://exampleB.com")
                .description(DESCRIPTION_B)
                .currency(Currency.USD)
                .seatsAvailable(3)
                .seatsTotal(8)
                .isRecurring(false)
                .build();
    }

    @Test
    void saveNewLicenseShouldSaveNewLicenseAndRelateItToCredentials() {
        when(licenseRepository.save(any())).thenReturn(CORRECT_NEW_LICENSE);
        when(modelMapper.map(any(), eq(License.class))).thenReturn(CORRECT_NEW_LICENSE);
        when(credentialsService.findByDTOs(any())).thenReturn(TEST_CREDENTIALS_LIST);

        licenseService.saveNewLicense(CORRECT_NEW_LICENSE_DTO);

        verify(licenseRepository, times(1)).save(any());
        verify(credentialsService, times(1)).findByDTOs(any());
        verify(licenseCredentialRepository, times(TEST_CREDENTIALS_LIST.size())).save(any());
    }

    @Test
    void getLicenseEditingDTOByNameCallsRepository(){
        licenseService.getLicenseEditingDTOByName(any());
        verify(licenseRepository).findLicenseByLicenseName(any());
    }

    @Test
    void editLicenseCallsSaveAndRelatesMethods(){
        when(licenseRepository.findById(any())).thenReturn(Optional.of(new License()));
        when(modelMapper.map(any(),any())).thenReturn(new License());

        licenseService.editLicense(newLicenseDTOTest);

        verify(licenseRepository).save(any());
        verify(modelMapper).map(any(),any());
        verify(licenseCredentialRepository).deleteAllByLicenseId(any());
        verify(credentialsService).findByDTOs(any());
    }

}
