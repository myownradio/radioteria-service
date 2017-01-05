package com.radioteria.test.business.services.userService;

import com.radioteria.db.entities.User;
import org.junit.Test;

public class UserDeleteTest extends AbstractUserServiceTest {

    @Test(expected = IllegalStateException.class)
    public void testUserDeleteCausesError() {

        userService.delete(new User());

    }

}
