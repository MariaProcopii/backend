package com.training.license.sharing.util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.training.license.sharing.dto.ExpiringLicenseDTO;
import com.training.license.sharing.dto.UnusedLicenseDTO;

import java.time.LocalDate;
import java.util.List;

public class LicenseTestData {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final ExpiringLicenseDTO ACTIVE_LICENSE = new ExpiringLicenseDTO(1L, "TestApp1", 1444.0, 120, LocalDate.of(2024, 1,7));
    public static final UnusedLicenseDTO UNUSED_LICENSE = new UnusedLicenseDTO(2L, "TestApp2", 700.0, 20);
    public static final UnusedLicenseDTO EXPIRED_LICENSE = new UnusedLicenseDTO(3L, "TestApp3", 800.0, 20);

    public static String getExpiredLicensesJson() throws JsonProcessingException {
        final List<UnusedLicenseDTO> expired = List.of(UNUSED_LICENSE, EXPIRED_LICENSE);
        return objectMapper.writeValueAsString(expired);
    }

    public static String getActiveLicensesJson() throws JsonProcessingException {
        final List<ExpiringLicenseDTO> active = List.of(ACTIVE_LICENSE);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(active);
    }
}

