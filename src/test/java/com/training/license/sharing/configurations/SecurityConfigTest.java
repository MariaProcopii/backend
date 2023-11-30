package com.training.license.sharing.configurations;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getAlLUsers_Authorized_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/users/get-all-users"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void getAlLUsers_Unauthorized_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/users/get-all-users"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    @Disabled("integrate the test properly")
    public void findById_Authorized_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void findById_Unauthorized_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    @Disabled("integrate the test properly")
    public void deactivateUsers_Authorized_ShouldReturn200() throws Exception {
        String validUserIds = "[1, 2, 3]";

        mockMvc.perform(put("/users/deactivate-user")
                        .contentType("application/json")
                        .content(validUserIds))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void deactivateUsers_Unauthorized_ShouldReturn403() throws Exception {
        String validUserIds = "[1, 2, 3]";

        mockMvc.perform(put("/users/deactivate-user")
                        .contentType("application/json")
                        .content(validUserIds))
                .andExpect(status().isForbidden());
    }

}
