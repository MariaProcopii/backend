package com.training.license.sharing.dto;

import com.training.license.sharing.entities.enums.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static com.training.license.sharing.util.CredentialTestData.getGenerateCredential;

class CredentialDTOTest {

    public static final String EMAIL_JOHNDOE_TEST = "john.doe@endava.com";
    public static final String PASSWORD_TEST = "johndoe";

    @Test
    void testEquals() {
        CredentialDTO credentialDTOTest1 = getGenerateCredential(EMAIL_JOHNDOE_TEST, PASSWORD_TEST, Role.USER);
        CredentialDTO credentialDTOTest2 = getGenerateCredential(EMAIL_JOHNDOE_TEST, PASSWORD_TEST, Role.USER);

        boolean obtainedResult = Objects.equals(credentialDTOTest1, credentialDTOTest2);

        Assertions.assertThat(obtainedResult).isTrue();
    }

    @Test
    void testHashCode() {
        CredentialDTO credentialDTOTest1 = getGenerateCredential(EMAIL_JOHNDOE_TEST, PASSWORD_TEST, Role.USER);
        CredentialDTO credentialDTOTest2 = getGenerateCredential(EMAIL_JOHNDOE_TEST, PASSWORD_TEST, Role.USER);

        int hashCodeOfDTOTest1 = Objects.hashCode(credentialDTOTest1);
        int hashCodeOfDTOTest2 = Objects.hashCode(credentialDTOTest2);

        Assertions.assertThat(hashCodeOfDTOTest1).isEqualTo(hashCodeOfDTOTest2);
    }
}