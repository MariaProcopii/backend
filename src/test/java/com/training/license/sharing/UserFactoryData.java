package com.training.license.sharing;

import com.training.license.sharing.entities.User;

public class UserFactoryData {
    private UserFactoryData(){}

    public static User createTestUser(long id){
        final User testUser = new User();
        testUser.setName("User" + id);
        testUser.setId(id);
        return testUser;
    }
}
