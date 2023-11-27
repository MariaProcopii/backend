package com.training.license.sharing.integrationTests;

import com.training.license.sharing.controllers.LicenseController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static com.training.license.sharing.util.LicenseTestData.getActiveLicensesJson;
import static com.training.license.sharing.util.LicenseTestData.getExpiredLicensesJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sqlMockData/create-mock-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sqlMockData/delete-mock-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LicensesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LicenseController licenseController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(licenseController).build();
    }

    @Test
    void shouldGetExpiringLicenses() throws Exception {
        mockMvc.perform(get("/license/get-expiring-licenses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(getActiveLicensesJson()));
    }

    @Test
    void shouldGetUnusedLicenses() throws Exception {
        mockMvc.perform(get("/license/get-unused-licenses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(getExpiredLicensesJson()));
    }
}
