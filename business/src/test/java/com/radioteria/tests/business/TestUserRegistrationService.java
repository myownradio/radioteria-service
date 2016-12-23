package com.radioteria.tests.business;

import com.radioteria.business.events.UserRegisteredEvent;
import com.radioteria.business.services.auth.api.UserService;
import com.radioteria.business.services.auth.impl.UserServiceImpl;
import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.User;
import com.radioteria.data.enumerations.UserState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/business-context.xml")
public class TestUserRegistrationService {

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<UserRegisteredEvent> eventCaptor;


    @Before
    public void setUp() {

        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");

    }

    @Test
    public void testUserRegistration() {

        UserService userRegistrationService = new UserServiceImpl(userDao, passwordEncoder, eventPublisher);

        userRegistrationService.register("foo@bar.com", "baz", "Foo Bar");

        verifyThatMethodsCalledAsExpected();
        captureTheArgumentsOrMethodCalls();

        verifyThatUserIsRegistered();
        verifyThatEventContainsRegisteredUser();

    }

    private void captureTheArgumentsOrMethodCalls() {

        verify(userDao).persist(userCaptor.capture());
        verify(eventPublisher).publishEvent(eventCaptor.capture());

    }

    private void verifyThatMethodsCalledAsExpected() {

        verify(userDao, only()).persist(isA(User.class));
        verify(eventPublisher, only()).publishEvent(isA(UserRegisteredEvent.class));

    }

    private void verifyThatUserIsRegistered() {

        User registeredUser = userCaptor.getValue();

        assertEquals("foo@bar.com", registeredUser.getEmail());
        assertEquals("Foo Bar", registeredUser.getName());
        assertEquals("encoded-password", registeredUser.getPassword());
        assertEquals(UserState.INACTIVE, registeredUser.getState());

    }

    private void verifyThatEventContainsRegisteredUser() {

        User registeredUser = userCaptor.getValue();
        UserRegisteredEvent capturedEvent = eventCaptor.getValue();

        assertSame(registeredUser, capturedEvent.getUser());

    }

}
