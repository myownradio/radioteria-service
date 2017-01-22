package com.radioteria.test.business.services.userService;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class UserRegistrationTest extends AbstractUserServiceTest {

    @Test
    public void testNewUserRegistration() {

//        when(userDao.isEmailAvailable(anyString())).thenReturn(true);
//
//        userService.register("foo@bar.com", "baz", "Foo Bar");
//
//        User user = captureUserThatBeenKept();
//
//        verifyCapturedUser(user);
//        verifyThatEventFiredWithUser(user);

    }

    @Test
    public void testUserRegistrationWhenUserExists() {

//        when(userDao.isEmailAvailable(anyString())).thenReturn(false);
//
//        userService.register("foo@bar.com", "baz", "Foo Bar");

    }

//    private User captureUserThatBeenKept() {
//
//        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
//
//        verify(userDao, times(1)).persist(argumentCaptor.capture());
//
//        return argumentCaptor.getValue();
//
//    }

//    private void verifyCapturedUser(User user) {
//
//        assertEquals("foo@bar.com", user.getEmail());
//        assertEquals("Foo Bar", user.getName());
//        assertEquals(UserState.INACTIVE, user.getState());
//        assertTrue(userService.isPasswordMatch("baz", user));
//
//    }

//    private void verifyThatEventFiredWithUser(User user) {
//
//        ArgumentCaptor<UserRegisteredEvent> argumentCaptor = ArgumentCaptor.forClass(UserRegisteredEvent.class);
//
//        verify(eventPublisher).publishEvent(argumentCaptor.capture());
//
//        UserRegisteredEvent capturedEvent = argumentCaptor.getValue();
//
//        assertSame(user, capturedEvent.getUser());
//
//    }

}
