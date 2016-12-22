package com.radioteria.tests.business;

import com.radioteria.business.services.auth.api.UserRegistrationService;
import com.radioteria.business.services.auth.impl.UserRegistrationServiceImpl;
import com.radioteria.data.dao.api.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/test-business-context.xml")
public class TestAuthService {

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserRegistrationService userRegistrationService;

    @Before
    public void setUp() {
        this.userRegistrationService = new UserRegistrationServiceImpl(userDao, passwordEncoder);
    }

    @Test
    public void testUserRegistration() {

    }
}
