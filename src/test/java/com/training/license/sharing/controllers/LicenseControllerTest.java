package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.ExpiringLicenseDTO;
import com.training.license.sharing.dto.UnusedLicenseDTO;
import com.training.license.sharing.services.LicenseService;
import com.training.license.sharing.validator.LicenseValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.training.license.sharing.util.NewLicenseTestData.CORRECT_NEW_LICENSE_JSON;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LicenseControllerTest {

    @Mock
    private LicenseService licenseService;

    @Mock
    private LicenseValidator licenseValidator;

    @InjectMocks
    private LicenseController licenseController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(licenseController).build();
    }

    @Test
    void shouldGetExpiringLicenses() throws Exception {
        final List<ExpiringLicenseDTO> licenses = new ArrayList<>();
        licenses.add(new ExpiringLicenseDTO());

        when(licenseService.getActiveLicenses()).thenReturn(licenses);

        mockMvc.perform(get("/license/get-expiring-licenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldGetUnusedLicenses() throws Exception {
        final List<UnusedLicenseDTO> licenses = new ArrayList<>();
        licenses.add(new UnusedLicenseDTO());

        when(licenseService.getExpiredLicenses()).thenReturn(licenses);

        mockMvc.perform(get("/license/get-unused-licenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldSaveNewLicensesWithCorrectJSON() throws Exception {
        mockMvc.perform(post("/license/add-new-license")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CORRECT_NEW_LICENSE_JSON))
                .andExpect(status().isOk());
    }

}
