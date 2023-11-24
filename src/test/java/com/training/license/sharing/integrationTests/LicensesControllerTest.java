package com.training.license.sharing.integrationTests;

import com.training.license.sharing.controllers.GlobalExceptionHandler;
import com.training.license.sharing.controllers.LicenseController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.training.license.sharing.util.LicenseTestData.LICENSE_NAME;
import static com.training.license.sharing.util.LicenseTestData.LICENSE_NAME_TESTAPP1;
import static com.training.license.sharing.util.LicenseTestData.LICENSE_NAME_TESTAPP2;
import static com.training.license.sharing.util.LicenseTestData.LICENSE_NAME_TESTAPP3;
import static com.training.license.sharing.util.LicenseTestData.NAME_PARAM;
import static com.training.license.sharing.util.LicenseTestData.VALUE_FOR_NAME_PARAM;
import static com.training.license.sharing.util.LicenseTestData.getActiveLicensesJson;
import static com.training.license.sharing.util.LicenseTestData.getExpiredLicensesJson;
import static com.training.license.sharing.util.LicenseTestData.getLicensesByNameJson;
import static com.training.license.sharing.util.NewLicenseTestData.CORRECT_NEW_LICENSE_JSON;
import static com.training.license.sharing.util.NewLicenseTestData.INCORRECT_CREDENTIALS_NEW_LICENSE_JSON;
import static com.training.license.sharing.util.NewLicenseTestData.INCORRECT_IMAGE_SIZE_NEW_LICENSE_JSON;
import static com.training.license.sharing.util.NewLicenseTestData.INCORRECT_IMAGE_TYPE_NEW_LICENSE_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(licenseController, globalExceptionHandler).build();
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

    @Test
    void shouldShouldReturnBadRequestWithIncorrectCredentialsJSON() throws Exception {
        mockMvc.perform(post("/license/add-new-license")
                        .contentType(APPLICATION_JSON)
                        .content(INCORRECT_CREDENTIALS_NEW_LICENSE_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldShouldReturnBadRequestWithIncorrectTypeImageJSON() throws Exception {
        mockMvc.perform(post("/license/add-new-license")
                        .contentType(APPLICATION_JSON)
                        .content(INCORRECT_IMAGE_TYPE_NEW_LICENSE_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldShouldReturnBadRequestWithIncorrectSizeImageJSON() throws Exception {
        mockMvc.perform(post("/license/add-new-license")
                        .contentType(APPLICATION_JSON)
                        .content(INCORRECT_IMAGE_SIZE_NEW_LICENSE_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSaveNewLicenseWithCorrectJSON() throws Exception {
        mockMvc.perform(post("/license/add-new-license")
                        .contentType(APPLICATION_JSON)
                        .content(CORRECT_NEW_LICENSE_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "Admin")
    void shouldGetAllLicensesSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/license/get-all-licenses")
                        .param(NAME_PARAM, VALUE_FOR_NAME_PARAM)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].licenseName").value(LICENSE_NAME_TESTAPP1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].licenseName").value(LICENSE_NAME_TESTAPP2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].licenseName").value(LICENSE_NAME_TESTAPP3));
    }

    @Test
    void shouldGetLicensesByName() throws Exception {
        mockMvc.perform(get("/license/get-all-licenses")
                        .param(NAME_PARAM, LICENSE_NAME))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(getLicensesByNameJson(LICENSE_NAME)));
    }

}
