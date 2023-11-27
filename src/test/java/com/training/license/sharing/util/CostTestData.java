package com.training.license.sharing.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.license.sharing.dto.CostViewDTO;
import com.training.license.sharing.dto.MonthCostDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CostTestData {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final CostViewDTO COST_VIEW = new CostViewDTO(
            null, null, 0, 0, 0, 0,
            List.of()
    );

    public static String getCostViewJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(COST_VIEW);
    }
}
