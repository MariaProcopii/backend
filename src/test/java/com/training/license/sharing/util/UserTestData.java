package com.training.license.sharing.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.training.license.sharing.dto.CredentialDTO;
import com.training.license.sharing.dto.UserDTO;
import com.training.license.sharing.entities.enums.DeliveryUnit;
import com.training.license.sharing.entities.enums.Discipline;
import com.training.license.sharing.entities.enums.Position;
import com.training.license.sharing.entities.enums.Role;
import com.training.license.sharing.entities.enums.Status;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserTestData {
    private static final CredentialDTO USER_1_CREDENTIAL = new CredentialDTO("test.user1@gmail.com", "test1", Role.USER);
    private static final CredentialDTO USER_2_CREDENTIAL = new CredentialDTO("test.user2@gmail.com", "test2", Role.USER);
    public static final UserDTO USER_1 = new UserDTO(1L, "USER_1", Position.DEVELOPER, Discipline.DEVELOPMENT, DeliveryUnit.MDD, Status.ACTIVE, 0, USER_1_CREDENTIAL);
    public static final UserDTO INVALID_USER_1 = new UserDTO(1L, "", Position.DEVELOPER, Discipline.DEVELOPMENT, DeliveryUnit.MDD, Status.ACTIVE, 0, USER_1_CREDENTIAL);
    public static final UserDTO USER_2 = new UserDTO(2L, "USER_2", Position.MANAGER, Discipline.TESTING, DeliveryUnit.MDD, Status.ACTIVE, 0, USER_2_CREDENTIAL);
    public static final int AMOUNT_OF_USERS = 2;
    public static final int AMOUNT_OF_NEW_USERS = 2;
    public static final int AMOUNT_OF_DISCIPLINES = 2;
    public static final Long VALID_ID = USER_1.getId();
    public static final Long INVALID_ID = 10L;

    public static String getAllUsersJson() throws JsonProcessingException {
        final ArrayList<UserDTO> users = new ArrayList<>(List.of(USER_1, USER_2));
        return new ObjectMapper().writeValueAsString(users);
    }

    public static String getUser1Json() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(USER_1);
    }

    public static String newUserToSaveJson() throws JsonProcessingException {
        final UserDTO saveUser = USER_1;
        USER_1.getCredential().setUsername("test.user3@gmail.com");
        USER_1.getCredential().setPassword("test1");
        return new ObjectMapper().writeValueAsString(saveUser);
    }

    public static String savedUserJson() throws JsonProcessingException {
        final UserDTO savedUser = USER_1;
        USER_1.getCredential().setPassword("dGVzdDE=");
        return new ObjectMapper().writeValueAsString(savedUser);
    }

    public static String getInvalidUser1Json() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(INVALID_USER_1);
    }

    public static String getDisciplinesWithUsersJson() throws JsonProcessingException {
        final Map<Discipline, Long> disciplinesWithUsers = new HashMap<>();
        disciplinesWithUsers.put(Discipline.DEVELOPMENT, 1L);
        disciplinesWithUsers.put(Discipline.TESTING, 1L);
        return new ObjectMapper().writeValueAsString(disciplinesWithUsers);
    }

    public static String getListUserIdsJson() throws JsonProcessingException {
        final List<Long> userIds = List.of(USER_1.getId(), USER_2.getId());
        return new ObjectMapper().writeValueAsString(userIds);
    }

    public static String getListInvalidUserIdJson() throws JsonProcessingException {
        final List<Long> invalidUserIds = List.of(10L);
        return new ObjectMapper().writeValueAsString(invalidUserIds);
    }
}
