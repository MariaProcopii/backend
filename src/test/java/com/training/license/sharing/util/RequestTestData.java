package com.training.license.sharing.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.training.license.sharing.dto.RequestDTO;
import com.training.license.sharing.dto.UserRequestDTO;
import com.training.license.sharing.entities.enums.RequestStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.training.license.sharing.util.UserTestData.USER_1;
import static com.training.license.sharing.util.UserTestData.USER_2;

public class RequestTestData {

    public static ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }
    public static final UserRequestDTO USER_REQUEST_1 = UserRequestDTO.builder()
            .requestId(1L)
            .status(RequestStatus.PENDING)
            .app("TestApp1")
            .requestDate(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT))
            .startOfUse(LocalDate.parse("2023-09-09"))
            .username(USER_1.getName())
            .discipline(USER_1.getDiscipline())
            .build();

    public static final UserRequestDTO USER_REQUEST_2 = UserRequestDTO.builder()
            .requestId(2L)
            .status(RequestStatus.REJECTED)
            .app("TestApp2")
            .requestDate(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT))
            .startOfUse(LocalDate.parse("2023-06-06"))
            .username(USER_2.getName())
            .discipline(USER_2.getDiscipline())
            .build();

    public static final int AMOUNT_OF_REQUESTS = 2;

    public static String getAllRequestsJson() throws JsonProcessingException {
        final List<UserRequestDTO> requests = new ArrayList<>(List.of(USER_REQUEST_2, USER_REQUEST_1));
        return objectMapper.writeValueAsString(requests);
    }

    public static String getValidRequestDTOJson() throws JsonProcessingException {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        final RequestDTO requestDTO = new RequestDTO(
                USER_REQUEST_1.getUsername(),
                USER_REQUEST_1.getDiscipline(),
                futureDate,
                USER_REQUEST_1.getApp()
        );
        return objectMapper.writeValueAsString(requestDTO);
    }

    public static String getInValidRequestDTOJson() throws JsonProcessingException {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        final RequestDTO invalidRequestDTO = new RequestDTO(
                "Invalid User",
                USER_REQUEST_1.getDiscipline(),
                pastDate,
                "Invalid App"
        );
        return objectMapper.writeValueAsString(invalidRequestDTO);
    }

    public static String getListValidIdsRequestJson() throws JsonProcessingException {
        final List<Long> requestIds = List.of(USER_REQUEST_1.getRequestId());
        return objectMapper.writeValueAsString(requestIds);
    }

    public static String getListInvalidIdsRequestJson() throws JsonProcessingException {
        final List<Long> invalidRequestIds = new ArrayList<>();
        return objectMapper.writeValueAsString(invalidRequestIds);
    }
}

