package com.radioteria.tests.business;

import com.radioteria.business.services.auth.api.UserRegistrationService;
import com.radioteria.business.services.auth.impl.UserRegistrationServiceImpl;
import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private UserRegistrationService userRegistrationService;

    @Before
    public void setUp() {

        userRegistrationService = new UserRegistrationServiceImpl(userDao, passwordEncoder);

        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");

    }

    @Test
    public void testUserRegistration() {

        userRegistrationService.register("foo@bar.com", "baz", "Foo Bar");

        verify(userDao, only()).persist(isA(User.class));

        verify(userDao).persist(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        assertEquals("foo@bar.com", capturedUser.getEmail());
        assertEquals("Foo Bar", capturedUser.getName());
        assertEquals("encoded-password", capturedUser.getPassword());

    }

}
