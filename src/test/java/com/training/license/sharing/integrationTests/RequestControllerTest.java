package com.training.license.sharing.integrationTests;

import com.training.license.sharing.controllers.RequestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.training.license.sharing.util.RequestTestData.AMOUNT_OF_REQUESTS;
import static com.training.license.sharing.util.RequestTestData.getAllRequestsJson;
import static com.training.license.sharing.util.RequestTestData.getInValidRequestDTOJson;
import static com.training.license.sharing.util.RequestTestData.getListInvalidIdsRequestJson;
import static com.training.license.sharing.util.RequestTestData.getListValidIdsRequestJson;
import static com.training.license.sharing.util.RequestTestData.getValidRequestDTOJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")

@Sql(value = {"/sqlMockData/create-mock-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sqlMockData/delete-mock-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestController requestController;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(requestController).build();
    }

    @Test
    void shouldFindAllRequests() throws Exception {
        mockMvc.perform(get("/requests/get-requests"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(AMOUNT_OF_REQUESTS))
                .andExpect(content().json(getAllRequestsJson()));
    }

    @Test
    void shouldRequestAccessForValidInput() throws Exception {

        mockMvc.perform(post("/requests/request-access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getValidRequestDTOJson()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotRequestAccessForInvalidInput() throws Exception {

        mockMvc.perform(post("/requests/request-access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getInValidRequestDTOJson()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldApproveAccessForValidInput() throws Exception {
        mockMvc.perform(put("/requests/approve-access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getListValidIdsRequestJson()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotApproveAccessForInvalidInput() throws Exception {
        mockMvc.perform(put("/requests/approve-access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getListInvalidIdsRequestJson()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectAccessForValidInput() throws Exception {
        mockMvc.perform(put("/requests/reject-access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getListValidIdsRequestJson()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectAccessForInvalidInput() throws Exception {
        mockMvc.perform(put("/requests/reject-access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getListInvalidIdsRequestJson()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}