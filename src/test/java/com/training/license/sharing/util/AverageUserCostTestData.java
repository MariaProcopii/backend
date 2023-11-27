package com.training.license.sharing.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.license.sharing.dto.AverageUserCostResponseDTO;
import com.training.license.sharing.dto.DisciplineCostDTO;

import java.util.Collections;

public class AverageUserCostTestData {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static final AverageUserCostResponseDTO AVERAGE_USER_COST_RESPONSE = createAverageUserCostResponse();

    private static AverageUserCostResponseDTO createAverageUserCostResponse() {
        DisciplineCostDTO testing = DisciplineCostDTO.builder()
                .disciplineName("TESTING")
                .averageCostsUserDiscipline(700)
                .build();

        return AverageUserCostResponseDTO.builder()
                .calculation(750)
                .disciplineCosts(Collections.singletonList(testing))
                .build();
    }

    public static String getAverageUserCostViewJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(AVERAGE_USER_COST_RESPONSE);
    }
}
