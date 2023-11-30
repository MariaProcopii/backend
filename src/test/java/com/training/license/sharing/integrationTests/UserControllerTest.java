package com.training.license.sharing.integrationTests;

import com.training.license.sharing.controllers.UserController;

import static com.training.license.sharing.util.UserTestData.AMOUNT_OF_DISCIPLINES;
import static com.training.license.sharing.util.UserTestData.AMOUNT_OF_NEW_USERS;
import static com.training.license.sharing.util.UserTestData.AMOUNT_OF_USERS;
import static com.training.license.sharing.util.UserTestData.INVALID_ID;
import static com.training.license.sharing.util.UserTestData.VALID_ID;
import static com.training.license.sharing.util.UserTestData.getAllUsersJson;
import static com.training.license.sharing.util.UserTestData.getDisciplinesWithUsersJson;
import static com.training.license.sharing.util.UserTestData.getInvalidUser1Json;
import static com.training.license.sharing.util.UserTestData.getListInvalidUserIdJson;
import static com.training.license.sharing.util.UserTestData.getListUserIdsJson;
import static com.training.license.sharing.util.UserTestData.getUser1Json;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.training.license.sharing.util.UserTestData.newUserToSaveJson;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sqlMockData/create-mock-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sqlMockData/delete-mock-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private UserController userController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/get-all-users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(AMOUNT_OF_USERS))
                .andExpect(content().json(getAllUsersJson()));
    }

    @Test
    void shouldSaveUserForValidInput() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/save-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserToSaveJson()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldNotSaveUserForInvalidInput() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/save-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getInvalidUser1Json()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFindUserForValidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", VALID_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(getUser1Json()));
    }

    @Test
    void shouldNotFindUserForInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", INVALID_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeactivateUsersIfAllIdsExist() throws Exception {
        final String status = "INACTIVE";

        mockMvc.perform(MockMvcRequestBuilders.put("/users/deactivate-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getListUserIdsJson()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(status))
                .andExpect(jsonPath("$[1].status").value(status));
    }

    @Test
    void shouldNotDeactivateUsersForInvalidIds() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users/deactivate-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getListInvalidUserIdJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldChangeRoleIfAllIdsExist() throws Exception {
        final String role = "ADMIN";

        mockMvc.perform(MockMvcRequestBuilders.put("/users/changing-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("role", role)
                        .content(getListUserIdsJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].credential.role").value(role))
                .andExpect(jsonPath("$[1].credential.role").value(role));
    }

    @Test
    void shouldNotChangeRoleForInvalidIds() throws Exception {
        final String role = "ADMIN";

        mockMvc.perform(MockMvcRequestBuilders.put("/users/changing-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("role", role)
                        .content(getListInvalidUserIdJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetTotalUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/get-total-users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(AMOUNT_OF_USERS));
    }

    @Test
    void shouldGetNewUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/get-new-users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(AMOUNT_OF_NEW_USERS));
    }

    @Test
    void shouldGetTotalDisciplines() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/get-total-disciplines")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(AMOUNT_OF_DISCIPLINES));
    }

    @Test
    void testGetTotalUsersPerDiscipline_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/get-disciplines-with-users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(content().json(getDisciplinesWithUsersJson()));
    }
}
