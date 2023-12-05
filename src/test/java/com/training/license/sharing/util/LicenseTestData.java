package com.training.license.sharing.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.training.license.sharing.dto.CredentialDTO;
import com.training.license.sharing.dto.ExpiringLicenseDTO;
import com.training.license.sharing.dto.LicenseSummaryDTO;
import com.training.license.sharing.dto.UnusedLicenseDTO;
import com.training.license.sharing.entities.enums.Currency;
import com.training.license.sharing.entities.enums.DurationUnit;
import com.training.license.sharing.entities.enums.LicenseType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.training.license.sharing.util.NewLicenseTestData.INCORRECT_CREDENTIALS_DTO_LIST;
import static com.training.license.sharing.util.NewLicenseTestData.INCORRECT_SIZE_IMAGE_IN_BASE64_CODE;
import static com.training.license.sharing.util.NewLicenseTestData.INCORRECT_TYPE_IMAGE_IN_BASE64_CODE;
import static com.training.license.sharing.util.NewLicenseTestData.TEST_CREDENTIALS_DTO_LIST;
import static com.training.license.sharing.util.NewLicenseTestData.toJsonList;
import static java.util.Optional.ofNullable;

public class LicenseTestData {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final ExpiringLicenseDTO EXPIRING_LICENSE_DTO = new ExpiringLicenseDTO(1L, "TestApp1", 1444.0, 120, LocalDate.of(2024, 1, 7));
    public static final UnusedLicenseDTO UNUSED_LICENSE_DTO1 = new UnusedLicenseDTO(2L, "TestApp2", 700.0, calculateUnusedPeriodForTesting(LocalDate.of(2023, 6, 6), 180));

    public static final UnusedLicenseDTO UNUSED_LICENSE_DTO2 = new UnusedLicenseDTO(3L, "TestApp3", 800.0, calculateUnusedPeriodForTesting(LocalDate.of(2023, 6, 6), 0));

    private static Integer calculateUnusedPeriodForTesting(LocalDate activationDate, Integer availability) {

        LocalDate expirationDate = activationDate.plusDays(availability);
        long unusedPeriod = ChronoUnit.DAYS.between(expirationDate, LocalDate.now());
        return Math.max((int) unusedPeriod, 0);
    }
    public static final LicenseSummaryDTO LICENSE_SUMMARY_DTO1 = new LicenseSummaryDTO("", "TestApp1", "Postman API", 1444.0, Currency.USD, 120, DurationUnit.MONTH, 10, 250, true, LocalDate.of(2024, 9, 9), LicenseType.SOFTWARE, false);
    public static final LicenseSummaryDTO LICENSE_SUMMARY_DTO2 = new LicenseSummaryDTO("", "TestApp2", "Adobe Studio API", 700.0, Currency.USD, 180, DurationUnit.MONTH, 10, 250, false, LocalDate.of(2023, 6, 10), LicenseType.TRAINING, false);
    public static final LicenseSummaryDTO LICENSE_SUMMARY_DTO3 = new LicenseSummaryDTO("", "TestApp3", "Postman API", 800.0, Currency.USD, 0, DurationUnit.YEAR, 10, 250, false, LocalDate.of(2023, 6, 6), LicenseType.TRAINING, false);

    public static final String LICENSE_NAME = "Postman";
    public static final String LICENSE_NAME_TESTAPP1 = "TestApp1";
    public static final String LICENSE_NAME_TESTAPP2 = "TestApp2";
    public static final String LICENSE_NAME_TESTAPP3 = "TestApp3";
    public static final String NAME_PARAM = "name";
    public static final String VALUE_FOR_NAME_PARAM = "TestApp";
    public static final String BAD_LICENSE_NAME = "BAD_LICENSE_NAME";
    public static final String LICENSE_NOT_EXISTENT_RETURNED_JSON = "{\n" +
            "    \"licenseName\": \"License with this name does not exist!\"\n" +
            "}";
    public static final String LICENSE_WITH_ID_NOT_EXISTENT_RETURNED_JSON = "{\n" +
            "    \"licenseId\": \"License with this id does not exist\"\n" +
            "}";
    public static final String LICENSE_WITH_NAME_ALREADY_EXISTENT_RETURNED_JSON = "{\n" +
            "    \"licenseName\": \"License with this name is already exists\"\n" +
            "}";

    public static final String CREDENTIALS_NON_EXISTENT_RETURNED_JSON = generateIncorrectCredentialJson(INCORRECT_CREDENTIALS_DTO_LIST.get(0));

    public static final String INCORRECT_LOGO_SIZE_RETURNED_JSON = "{\n" +
            "    \"logo\": \"Logo Image Size must be be between 2 and 10 MB\"\n" +
            "}";

    public static final String INCORRECT_TYPE_TYPE_RETURNED_JSON = "{\n" +
            "    \"logo\": \"Image must have JPEG/JPG/PNG type\"\n" +
            "}";

    private static String generateIncorrectCredentialJson(CredentialDTO credentialDTO) {
        return "{\n" +
                "    \"credentials\": \"User " + credentialDTO.getUsername() + " with this credentials does not exist\"\n" +
                "}";
    }

    private static final String LICENSE_TESTAPP1_WEBSITE = "www.testapp1.com";
    public static final String CORRECT_JSON_TESTAPP1 = generateEditingJSON(1L, LICENSE_NAME_TESTAPP1, LICENSE_TESTAPP1_WEBSITE, null, TEST_CREDENTIALS_DTO_LIST);

    public static final String INCORRECT_ID_JSON_TESTAPP1 = generateEditingJSON(121L, LICENSE_NAME_TESTAPP1, LICENSE_TESTAPP1_WEBSITE, null, TEST_CREDENTIALS_DTO_LIST);
    public static final String INCORRECT_LOGO_SIZE_JSON_TESTAPP1 = generateEditingJSON(1L, LICENSE_NAME_TESTAPP1, LICENSE_TESTAPP1_WEBSITE, INCORRECT_SIZE_IMAGE_IN_BASE64_CODE, TEST_CREDENTIALS_DTO_LIST);
    public static final String INCORRECT_LOGO_TYPE_JSON_TESTAPP1 = generateEditingJSON(1L, LICENSE_NAME_TESTAPP1, LICENSE_TESTAPP1_WEBSITE, INCORRECT_TYPE_IMAGE_IN_BASE64_CODE, TEST_CREDENTIALS_DTO_LIST);
    public static final String ALREADY_EXISTENT_NAME_JSON_TESTAPP1 = generateEditingJSON(1L, LICENSE_NAME_TESTAPP3, LICENSE_TESTAPP1_WEBSITE, null, TEST_CREDENTIALS_DTO_LIST);
    ;
    public static final String INCORRECT_CREDENTIALS_JSON_TESTAPP1 = generateEditingJSON(1L, LICENSE_NAME_TESTAPP1, LICENSE_TESTAPP1_WEBSITE, null, INCORRECT_CREDENTIALS_DTO_LIST);
    ;

    private static String generateEditingJSON(Long id, String name, String website, String logo, List<CredentialDTO> credentialDTOS) {
        List<String> credentialDTOJsons = toJsonList(credentialDTOS);
        String credentialJsonList = String.join(",", credentialDTOJsons);
        logo = wrapStringValueByQuotesForJson(logo);
        website = wrapStringValueByQuotesForJson(website);
        name = wrapStringValueByQuotesForJson(name);

        return "{\n" +
                "    \"licenseId\": " + id + ",\n" +
                "    \"licenseName\": " + name + ",\n" +
                "    \"website\": " + website + ",\n" +
                "    \"description\": \"Postman API\",\n" +
                "    \"logo\": " + logo + ",\n" +
                "    \"credentials\" : [" + credentialJsonList + "],\n" +
                "    \"cost\": 1444.0,\n" +
                "    \"currency\": \"USD\",\n" +
                "    \"availability\": 120,\n" +
                "    \"isRecurring\": false,\n" +
                "    \"seatsTotal\": 250,\n" +
                "    \"seatsAvailable\": 20,\n" +
                "    \"licenseType\": \"SOFTWARE\",\n" +
                "    \"expiresOn\": \"09-Sep-2023\"\n" +
                "}";
    }

    private static String wrapStringValueByQuotesForJson(String value) {
        return ofNullable(value)
                .map(val -> "\"" + val + "\"")
                .orElse(null);
    }

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

