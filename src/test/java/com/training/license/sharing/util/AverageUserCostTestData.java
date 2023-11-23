package com.training.license.sharing.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.license.sharing.dto.AverageUserCostViewDTO;

import java.util.List;

public class AverageUserCostTestData {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final AverageUserCostViewDTO AVERAGE_USER_COST_VIEW = new AverageUserCostViewDTO(750, "TESTING", 700);

    public static String getAverageUserCostViewJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(List.of(AVERAGE_USER_COST_VIEW));
    }
}
