package com.training.license.sharing;

import com.training.license.sharing.entities.Credential;
import com.training.license.sharing.entities.User;
import com.training.license.sharing.entities.enums.Role;

public class UserFactoryData {

    private static final String PASSWORD = "USER_TEST_PASSWORD";

    private UserFactoryData() {
    }

    public static User createTestUser(long id){
        final User testUser = new User();
        testUser.setName("User" + id);
        testUser.setId(id);
        testUser.setCredential(getTestCredential(id));
        return testUser;
    }

    private static Credential getTestCredential(long id) {
        Credential testCredential = new Credential();
        testCredential.setId(id);
        testCredential.setRole(Role.USER);
        testCredential.setPassword(PASSWORD);
        return testCredential;
    }
}
