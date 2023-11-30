package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.CostViewDTO;
import com.training.license.sharing.dto.MonthCostDTO;
import com.training.license.sharing.services.CostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CostControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CostService service;

    @InjectMocks
    private CostController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getCost_WhenSuccessful_ShouldReturnCostData2022() throws Exception {
        CostViewDTO costViewDTO = new CostViewDTO(4200, 1900, 2, 1, 2, 1 , Arrays.asList(
                new MonthCostDTO("January 22", 300),
                new MonthCostDTO("February 22", 350)
        ));
        when(service.getCosts()).thenReturn(costViewDTO);
        mockMvc.perform(get("/cost/get-cost")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCosts2022").value(4200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getCost_WhenServiceException_ShouldReturnInternalServerError() throws Exception {
        final String exceptionMessage = "Service exception";
        when(service.getCosts()).thenThrow(new RuntimeException(exceptionMessage));
        mockMvc.perform(get("/cost/get-cost")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(exceptionMessage));
    }

    @Test
    void getCost_WhenNoData_ShouldReturnEmptyResponse() throws Exception {
        when(service.getCosts()).thenReturn(null);
        mockMvc.perform(get("/cost/get-cost")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
