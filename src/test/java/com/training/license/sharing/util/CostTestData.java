package com.training.license.sharing.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.license.sharing.dto.CostViewDTO;

import java.util.List;

public class CostTestData {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final CostViewDTO COST_VIEW = new CostViewDTO(4200, 0, 2, 0, 2, 0);

    public static String getCostViewJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(List.of(COST_VIEW));
    }
}
