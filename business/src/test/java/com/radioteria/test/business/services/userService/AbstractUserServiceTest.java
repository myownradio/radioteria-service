package com.radioteria.test.business.services.userService;

import com.radioteria.business.services.api.UserService;
import com.radioteria.business.services.impl.UserServiceImpl;
import com.radioteria.data.dao.api.UserDao;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/business-context.xml")
abstract public class AbstractUserServiceTest {

    @Mock
    protected UserDao userDao;

    @Mock
    protected ApplicationEventPublisher eventPublisher;


    protected UserService userService;

    @Before
    public void setup() {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        userService = new UserServiceImpl(userDao, passwordEncoder, eventPublisher);

    }

}
