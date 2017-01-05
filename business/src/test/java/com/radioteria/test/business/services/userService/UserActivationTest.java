package com.radioteria.test.business.services.userService;

import com.radioteria.business.events.userService.UserConfirmedEvent;
import com.radioteria.business.services.user.exceptions.UserNotFoundException;
import com.radioteria.db.entities.User;
import com.radioteria.db.enumerations.UserState;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

public class UserActivationTest extends AbstractUserServiceTest {

    @Test
    public void testActivation() {

        User user = createInactiveUser();

        configureDaoToReturnGivenUser(user);

        userService.activateByEmail("foo@bar.com");

        verifyThatUserIsActivated(user);
        verifyThatEventFiredWithUser(user);

    }

    @Test(expected = UserNotFoundException.class)
    public void testActivationWhenUserDoesNotExist() {

        configureDaoToReturnNull();

        userService.activateByEmail("foo@bar.com");

    }

    private User createInactiveUser() {

        User user = new User("foo@bar.com");
        user.setState(UserState.INACTIVE);

        return user;

    }

    private void configureDaoToReturnGivenUser(User user) {

        when(userDao.findByEmail(anyString())).thenReturn(Optional.of(user));

    }

    private void configureDaoToReturnNull() {

        when(userDao.findByEmail(anyString())).thenReturn(Optional.empty());

    }

    private void verifyThatUserIsActivated(User user) {

        assertEquals(UserState.ACTIVE, user.getState());

    }

    private void verifyThatEventFiredWithUser(User user) {

        ArgumentCaptor<UserConfirmedEvent> eventCaptor = ArgumentCaptor.forClass(UserConfirmedEvent.class);

        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

        UserConfirmedEvent capturedEvent = eventCaptor.getValue();

        assertSame(user, capturedEvent.getUser());

    }


}
