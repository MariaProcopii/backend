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

import static com.training.license.sharing.util.CostTestData.EXCEPTION_MESSAGE;
import static com.training.license.sharing.util.CostTestData.EXCEPTION_MESSAGE_JSON;
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
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getCost_WhenSuccessful_ShouldReturnCostData2023() throws Exception {
        CostViewDTO costViewDTO = new CostViewDTO(4200, 1900, 2, 1, 2, 1 , Arrays.asList(
                new MonthCostDTO("January 23", 300),
                new MonthCostDTO("February 23", 350)
        ));
        when(service.getCosts()).thenReturn(costViewDTO);
        mockMvc.perform(get("/cost/get-cost")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCostsCurrentYear").value(4200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getCost_WhenServiceException_ShouldReturnInternalServerError() throws Exception {
        when(service.getCosts()).thenThrow(new RuntimeException(EXCEPTION_MESSAGE));
        mockMvc.perform(get("/cost/get-cost")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(EXCEPTION_MESSAGE_JSON));
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
