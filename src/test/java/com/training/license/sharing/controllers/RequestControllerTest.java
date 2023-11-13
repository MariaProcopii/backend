package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.UserRequestDTO;
import com.training.license.sharing.services.RequestService;
import com.training.license.sharing.validator.RequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {
    @InjectMocks
    private RequestController requestController;
    @Mock
    private RequestService requestService;

    @Mock
    private RequestValidator requestValidator;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(requestController).build();
    }

    @Test
    void findAllShouldReturnAllElemsTest() throws Exception {
        final List<UserRequestDTO> expectedList = asList(new UserRequestDTO(), new UserRequestDTO());

        when(requestService.findAll(null, null)).thenReturn(expectedList);
        mockMvc.perform(get("/requests/get-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        verify(requestService).findAll(null, null);
    }

    @Test
    void approveAccessShouldReturnStatusOkTest() throws Exception {
        final String json = "[1, 2]";
        final List<Long> list = asList(1L, 2L);

        mockMvc.perform(put("/requests/approve-access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
        verify(requestService).approveRequest(list);
        verify(requestValidator).validateRequestAccessApproval(any(), any());
    }

    @Test
    void rejectAccessShouldReturnStatusOkTest() throws Exception {
        final String json = "[1, 2]";
        final List<Long> list = asList(1L, 2L);

        mockMvc.perform(put("/requests/reject-access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
        verify(requestService).rejectRequest(list);
    }

    @Test
    void requestAccessShouldReturnStatusOkTest() throws Exception {
        final String json = "{\n" +
                "    \"username\" : \"John Doe\",\n" +
                "    \"discipline\": \"DEVELOPMENT\",\n" +
                "    \"app\" : \"JetBrains\",\n" +
                "    \"startOfUse\" : \"05-Jun-2023\"\n" +
                "}";

        mockMvc.perform(post("/requests/request-access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
        verify(requestValidator).validateRequestAccess(any(), any());
    }

}