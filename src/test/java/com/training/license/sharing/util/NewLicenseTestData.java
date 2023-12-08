package com.training.license.sharing.util;

import com.training.license.sharing.dto.CredentialDTO;
import com.training.license.sharing.dto.NewLicenseDTO;
import com.training.license.sharing.entities.Credential;
import com.training.license.sharing.entities.License;
import com.training.license.sharing.entities.enums.LicenseType;
import com.training.license.sharing.entities.enums.Role;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

public class NewLicenseTestData {

    public static String INCORRECT_TYPE_IMAGE_IN_BASE64_CODE;

    public static String CORRECT_LOGO;

    public static final String INCORRECT_SIZE_IMAGE_IN_BASE64_CODE;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("src/test/resources/testing_data.properties"));
            INCORRECT_TYPE_IMAGE_IN_BASE64_CODE = properties.getProperty("logo.incorrecrimage");
            INCORRECT_SIZE_IMAGE_IN_BASE64_CODE = properties.getProperty("logo.small");
            CORRECT_LOGO = properties.getProperty("logo.twomb");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final List<Credential> TEST_CREDENTIALS_LIST = List.of(new Credential(0L, "john.doe@endava.com", "johndoe", Role.USER, null));

    public static final List<CredentialDTO> INCORRECT_CREDENTIALS_DTO_LIST = List.of(new CredentialDTO("INCORRECT@endava.com", "password", null));

    public static final List<CredentialDTO> TEST_CREDENTIALS_DTO_LIST = List.of(new CredentialDTO("test.user1@gmail.com", "test1", Role.USER));

    public static final NewLicenseDTO INCORRECT_TYPE_OF_IMAGE_NEW_LICENSE_DTO = getNewLicenseDTO(INCORRECT_TYPE_IMAGE_IN_BASE64_CODE, TEST_CREDENTIALS_DTO_LIST);

    public static final NewLicenseDTO CORRECT_NEW_LICENSE_DTO = getNewLicenseDTO(CORRECT_LOGO, TEST_CREDENTIALS_DTO_LIST);

    public static final NewLicenseDTO INCORRECT_SIZE_OF_IMAGE_NEW_LICENSE_DTO = getNewLicenseDTO(INCORRECT_SIZE_IMAGE_IN_BASE64_CODE, TEST_CREDENTIALS_DTO_LIST);

    public static final NewLicenseDTO INCORRECT_CREDENTIALS_NEW_LICENSE_DTO = getNewLicenseDTO(CORRECT_LOGO, INCORRECT_CREDENTIALS_DTO_LIST);

    public static final License CORRECT_NEW_LICENSE = generateLicense();

    private static NewLicenseDTO getNewLicenseDTO(String logo, List<CredentialDTO> credentialDTOS) {
        return new NewLicenseDTO().toBuilder()
                .licenseName("Sample License TEST")
                .activationDate(LocalDate.now())
                .availability(20)
                .cost(20.3)
                .seats(20)
                .logo(logo)
                .description("TEST DESCR")
                .website("www.testwebsite.com")
                .isRecurring(true)
                .typeOfLicense(LicenseType.SOFTWARE)
                .credentials(credentialDTOS)
                .build();
    }

    private static License generateLicense() {
        return new License().toBuilder()
                .id(0L)
                .licenseName("Sample License TEST")
                .activationDate(LocalDate.now())
                .availability(20)
                .cost(20.3)
                .seatsTotal(20)
                .logo(NewLicenseTestData.CORRECT_LOGO.getBytes())
                .description("TEST DESCR")
                .website("www.testwebsite.com")
                .isRecurring(true)
                .licenseType(LicenseType.SOFTWARE)
                .build();
    }


    public static final String CORRECT_NEW_LICENSE_JSON = generateJsonNewLicenseDTO(CORRECT_LOGO, TEST_CREDENTIALS_DTO_LIST);

    public static final String INCORRECT_CREDENTIALS_NEW_LICENSE_JSON = generateJsonNewLicenseDTO(CORRECT_LOGO, INCORRECT_CREDENTIALS_DTO_LIST);

    public static final String INCORRECT_IMAGE_TYPE_NEW_LICENSE_JSON = generateJsonNewLicenseDTO(INCORRECT_TYPE_IMAGE_IN_BASE64_CODE, TEST_CREDENTIALS_DTO_LIST);

    public static final String INCORRECT_IMAGE_SIZE_NEW_LICENSE_JSON = generateJsonNewLicenseDTO(INCORRECT_SIZE_IMAGE_IN_BASE64_CODE, TEST_CREDENTIALS_DTO_LIST);

    private static String generateJsonNewLicenseDTO(String logo, List<CredentialDTO> credentialDTOS) {
        List<String> credentialDTOJsons = toJsonList(credentialDTOS);
        String credentiaJsonlList = String.join(",", credentialDTOJsons);

        return "{\n" +
                "    \n" +
                "    \"licenseName\": \"Sample License 1\",\n" +
                "    \"description\": \"This is a sample license description with a call-to-action.\",\n" +
                "    \"website\" : \"www.sambplelicense.com\",\n" +
                "    \"cost\": 22,\n" +
                "    \"currency\": \"USD\",\n" +
                "    \"availability\": 365,\n" +
                "    \"seats\": 100,\n" +
                "    \"isActive\": true,  \n" +
                "    \"expiresOn\": \"12-May-2023\",\n" +
                "    \"licenseType\": \"TRAINING\",\n" +
                "    \"isRecurring\": \"true\",\n" +
                "    \"credentials\" : ["+ credentiaJsonlList +"],\n" +
                "\n" +
                "    \"logo\" : \"" + logo + "\"\n" +
                "}";
    }

    public static List<String> toJsonList(List<CredentialDTO> credentialDTOS) {
        return credentialDTOS.stream().map(NewLicenseTestData::toJson).toList();
    }

    public static String toJson(CredentialDTO credentialDTO) {
        return "{ \"username\" : \"" + credentialDTO.getUsername() + "\", \"password\" : \"" + credentialDTO.getPassword() + "\"}";
    }
}
