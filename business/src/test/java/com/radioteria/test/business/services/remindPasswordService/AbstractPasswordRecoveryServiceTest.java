package com.radioteria.test.business.services.remindPasswordService;

import com.radioteria.business.services.user.api.PasswordRecoveryService;
import com.radioteria.business.services.user.api.UserService;
import com.radioteria.business.services.user.impl.PasswordRecoveryServiceImpl;
import com.radioteria.db.repositories.api.UserDao;
import com.radioteria.db.enumerations.UserState;
import com.radioteria.support.services.mail.EmailService;
import com.radioteria.support.template.TemplateService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
abstract public class AbstractPasswordRecoveryServiceTest {

    @Mock
    protected UserDao userDao;

    @Mock
    protected EmailService emailService;

    @Mock
    protected TemplateService templateService;

    @Mock
    protected UserService userService;


    protected PasswordRecoveryService passwordRecoveryService;

    @Before
    public void setup() {
        User user = new User();
        user.setState(UserState.ACTIVE);
        user.setEmail("foo@bar.com");
        user.setPassword("");
        user.setName("Name");
        user.setId(1L);

        when(userDao.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userDao.find(anyLong())).thenReturn(Optional.empty());

        when(userDao.findByEmail("foo@bar.com")).thenReturn(Optional.of(user));
        when(userDao.find(1L)).thenReturn(Optional.of(user));

        passwordRecoveryService = new PasswordRecoveryServiceImpl(userDao, emailService, templateService, userService);
    }
}
