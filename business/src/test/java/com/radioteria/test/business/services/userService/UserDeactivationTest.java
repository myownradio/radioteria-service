package com.radioteria.test.business.services.userService;

import com.radioteria.db.enumerations.UserState;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class UserDeactivationTest extends AbstractUserServiceTest {

    @Test
    public void testUserDeactivation() {

        User user = Mockito.mock(User.class);

        userService.deactivate(user);

        verify(user, times(1)).setState(UserState.DELETED);

    }

}
