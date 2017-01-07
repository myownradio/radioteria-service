package com.radioteria.test.business.services.userService;

import com.radioteria.business.events.userService.PasswordChangedEvent;
import com.radioteria.db.entities.User;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserChangePasswordTest extends AbstractUserServiceTest {

    @Test
    public void testUserChangePassword() {

        User user = new User();
        String newPassword = "new password";

        userService.changePassword(user, newPassword);

        verifyThatPasswordChangedSuccessfully(user, newPassword);
        verifyThatEventFiredWithUser(user);

    }

    private void verifyThatPasswordChangedSuccessfully(User user, String newPassword) {

        assertTrue(userService.isPasswordMatch(newPassword, user));

    }

    private void verifyThatEventFiredWithUser(User user) {

        ArgumentCaptor<PasswordChangedEvent> argumentCaptor =
                ArgumentCaptor.forClass(PasswordChangedEvent.class);

        verify(eventPublisher, times(1)).publishEvent(argumentCaptor.capture());

        PasswordChangedEvent passwordChangedEvent = argumentCaptor.getValue();

        assertSame(user, passwordChangedEvent.getUser());

    }

}
