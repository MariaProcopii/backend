package com.training.license.sharing.integrationTests;

import com.training.license.sharing.controllers.AverageUserCostController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static com.training.license.sharing.util.AverageUserCostTestData.getAverageUserCostViewJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-mock-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-mock-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AverageUserCostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AverageUserCostController averageUserCostController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(averageUserCostController).build();
    }

    @Test
    void shouldGetAverageUserCost() throws Exception {
        mockMvc.perform(get("/average-user-cost/get-average-user-cost"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(getAverageUserCostViewJson()));
    }
}
