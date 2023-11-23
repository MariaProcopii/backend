package com.training.license.sharing.util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.license.sharing.dto.LicenseDTO;

import java.util.List;

public class LicenseTestData {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final LicenseDTO ACTIVE_LICENSE = new LicenseDTO("TestApp1", 1444, 120, 0);
    public static final LicenseDTO UNUSED_LICENSE = new LicenseDTO("TestApp2", 700, 180, 20);
    public static final LicenseDTO EXPIRED_LICENSE = new LicenseDTO("TestApp3", 800, 0, 20);

    public static String getExpiredLicensesJson() throws JsonProcessingException {
        final List<LicenseDTO> expired = List.of(UNUSED_LICENSE, EXPIRED_LICENSE);
        return objectMapper.writeValueAsString(expired);
    }

    public static String getActiveLicensesJson() throws JsonProcessingException {
        final List<LicenseDTO> active = List.of(ACTIVE_LICENSE);
        return objectMapper.writeValueAsString(active);
    }
}

