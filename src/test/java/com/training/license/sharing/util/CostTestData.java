package com.training.license.sharing.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.license.sharing.dto.CostViewDTO;
import com.training.license.sharing.dto.MonthCostDTO;

import java.util.List;

public class CostTestData {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final MonthCostDTO MONTH_DTO1 = new MonthCostDTO("Jun 23" , 1500);
    public static final MonthCostDTO MONTH_DTO2 = new MonthCostDTO("Sep 23" , 1444);
    public static final CostViewDTO COST_VIEW = new CostViewDTO(
            2944, null, 1, 1, 2, 2,
            List.of(MONTH_DTO1 , MONTH_DTO2)
    );
    public final static String EXCEPTION_MESSAGE_JSON = "{\"invalid json\":\"" + "Service exception" + "\"}";
    public final static String EXCEPTION_MESSAGE = "Service exception";

    public static String getCostViewJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(COST_VIEW);
    }
}
