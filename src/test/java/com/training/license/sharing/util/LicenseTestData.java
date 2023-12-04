package com.training.license.sharing.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.training.license.sharing.dto.ExpiringLicenseDTO;
import com.training.license.sharing.dto.LicenseSummaryDTO;
import com.training.license.sharing.dto.UnusedLicenseDTO;
import com.training.license.sharing.entities.enums.Currency;
import com.training.license.sharing.entities.enums.DurationUnit;
import com.training.license.sharing.entities.enums.LicenseType;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class LicenseTestData {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final ExpiringLicenseDTO EXPIRING_LICENSE_DTO = new ExpiringLicenseDTO(1L, "TestApp1", 1444.0, 120, LocalDate.of(2024, 1, 7));
    public static final UnusedLicenseDTO UNUSED_LICENSE_DTO1 = new UnusedLicenseDTO(2L, "TestApp2", 700.0, 20);
    public static final UnusedLicenseDTO UNUSED_LICENSE_DTO2 = new UnusedLicenseDTO(3L, "TestApp3", 800.0, 20);
    public static final LicenseSummaryDTO LICENSE_SUMMARY_DTO1 = new LicenseSummaryDTO("", "TestApp1", "Postman API", 1444.0, Currency.USD, 120, DurationUnit.MONTH, 10, 250, true, LocalDate.of(2024, 9, 9), LicenseType.SOFTWARE, false);
    public static final LicenseSummaryDTO LICENSE_SUMMARY_DTO2 = new LicenseSummaryDTO("", "TestApp2", "Adobe Studio API", 700.0, Currency.USD, 180, DurationUnit.MONTH, 10, 250, false, LocalDate.of(2023, 6, 10), LicenseType.TRAINING, false);
    public static final LicenseSummaryDTO LICENSE_SUMMARY_DTO3 = new LicenseSummaryDTO("", "TestApp3", "Postman API", 800.0, Currency.USD, 0, DurationUnit.YEAR, 10, 250, false, LocalDate.of(2023, 6, 6), LicenseType.TRAINING, false);

    public static final String LICENSE_NAME = "Postman";
    public static final String LICENSE_NAME_TESTAPP1 = "TestApp1";
    public static final String LICENSE_NAME_TESTAPP2 = "TestApp2";
    public static final String LICENSE_NAME_TESTAPP3 = "TestApp3";
    public static final String NAME_PARAM = "name";
    public static final String VALUE_FOR_NAME_PARAM = "TestApp";

    public static String getExpiredLicensesJson() throws JsonProcessingException {
        final List<UnusedLicenseDTO> expired = List.of(UNUSED_LICENSE_DTO1, UNUSED_LICENSE_DTO2);
        return objectMapper.writeValueAsString(expired);
    }

    public static String getActiveLicensesJson() throws JsonProcessingException {
        final List<ExpiringLicenseDTO> active = List.of(EXPIRING_LICENSE_DTO);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(active);
    }

    public static String getLicensesByNameJson(String name) throws JsonProcessingException {
        final List<LicenseSummaryDTO> allLicenses = List.of(LICENSE_SUMMARY_DTO1, LICENSE_SUMMARY_DTO2, LICENSE_SUMMARY_DTO3);

        final List<LicenseSummaryDTO> filteredLicenses = allLicenses.stream()
                .filter(license -> license.getLicenseName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(filteredLicenses);
    }
}

