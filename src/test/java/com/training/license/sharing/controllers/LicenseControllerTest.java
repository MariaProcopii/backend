package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.LicenseDTO;
import com.training.license.sharing.services.LicenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LicenseControllerTest {

    @Mock
    private LicenseService licenseService;

    @InjectMocks
    private LicenseController licenseController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(licenseController).build();
    }

    @Test
    public void shouldGetExpiringLicenses() throws Exception {
        final List<LicenseDTO> licenses = new ArrayList<>();
        licenses.add(new LicenseDTO());

        when(licenseService.getActiveLicenses(1L)).thenReturn(licenses);

        mockMvc.perform(get("/license/get-expiring-licenses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldGetUnusedLicenses() throws Exception {
        final List<LicenseDTO> licenses = new ArrayList<>();
        licenses.add(new LicenseDTO());

        when(licenseService.getExpiredLicenses(1L)).thenReturn(licenses);

        mockMvc.perform(get("/license/get-unused-licenses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
