package com.training.license.sharing;

import com.training.license.sharing.entities.Credential;
import com.training.license.sharing.entities.User;
import com.training.license.sharing.entities.enums.Role;

public class UserFactoryData {
    private UserFactoryData(){}

    public static User createTestUser(long id){
        final User testUser = new User();
        testUser.setName("User" + id);
        testUser.setId(id);
        testUser.setCredential(getTestCredential(id, testUser));
        return testUser;
    }

    private static Credential getTestCredential(long id, User testUser) {
        Credential testCredential = new Credential();
        testCredential.setId(id);
        testCredential.setRole(Role.USER);
        return testCredential;
    }
}
