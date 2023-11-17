package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.AverageUserCostViewDTO;
import com.training.license.sharing.services.AverageUserCostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AverageUserCostControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AverageUserCostService service;

    @InjectMocks
    private AverageUserCostController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAverageUserCost_WhenSuccessful_ShouldReturnDevelopmentCostData() throws Exception {
        when(service.getAverageUserCosts()).thenReturn(Collections.singletonList(
                new AverageUserCostViewDTO(100, "Development", 2000)));
        mockMvc.perform(get("/average-user-cost/get-average-user-cost")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].disciplineName").value("Development"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAverageUserCost_WhenNoData_ShouldReturnEmptyResponse() throws Exception {
        when(service.getAverageUserCosts()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/average-user-cost/get-average-user-cost")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAverageUserCost_WhenServiceException_ShouldReturnInternalServerError() throws Exception {
        final String exceptionMessage = "Service exception";
        when(service.getAverageUserCosts()).thenThrow(new RuntimeException(exceptionMessage));
        mockMvc.perform(get("/average-user-cost/get-average-user-cost")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(exceptionMessage));
    }
}