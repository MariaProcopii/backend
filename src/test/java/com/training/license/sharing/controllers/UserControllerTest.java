package com.training.license.sharing.controllers;

import com.training.license.sharing.services.UserService;
import com.training.license.sharing.validator.UserValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private UserValidation userValidation;

    @InjectMocks
    private UserController userController;
    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    void findByIdShouldReturnNotFound() throws Exception {
        final Long userId = 3L;

        when(userService.getUserById(userId)).thenReturn(Optional.empty());
        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void changeRoleShouldReturnBadRequest() throws Exception {
        final String json = "[1, 2, 3, 4]";
        final String invalidRole = "INVALID_ROLE";

        mockMvc.perform(put("/users/changing-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .param("role", invalidRole))
                .andExpect(status().isBadRequest());
    }
}