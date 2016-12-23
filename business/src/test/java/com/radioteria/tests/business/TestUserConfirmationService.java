package com.radioteria.tests.business;

import com.radioteria.business.events.UserConfirmedEvent;
import com.radioteria.business.services.auth.api.UserService;
import com.radioteria.business.services.auth.impl.UserServiceImpl;
import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.User;
import com.radioteria.data.enumerations.UserState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/business-context.xml")
public class TestUserConfirmationService {

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Captor
    private ArgumentCaptor<UserConfirmedEvent> eventCaptor;

    @Test
    public void testUserConfirmation() {

        UserService userRegistrationService = new UserServiceImpl(userDao, passwordEncoder, eventPublisher);
        User user = createInactiveUser();

        configureDaoToReturnGivenUser(user);

        userRegistrationService.confirm("foo@bar.com");

        verifyThatUserIsActivated(user);
        verifyThatConfirmatiopnEventIsFired();
        captureTheFiredEvent();
        verifyThatEventContainsConfirmedUser(user);

    }

    private User createInactiveUser() {

        User user = new User("foo@bar.com");
        user.setState(UserState.INACTIVE);

        return user;

    }

    private void configureDaoToReturnGivenUser(User user) {

        when(userDao.findByEmail(anyString())).thenReturn(user);

    }

    private void verifyThatUserIsActivated(User user) {

        assertEquals(UserState.ACTIVE, user.getState());

    }

    private void verifyThatConfirmatiopnEventIsFired() {

        verify(eventPublisher, only()).publishEvent(isA(UserConfirmedEvent.class));

    }

    private void captureTheFiredEvent() {

        verify(eventPublisher).publishEvent(eventCaptor.capture());

    }

    private void verifyThatEventContainsConfirmedUser(User user) {

        UserConfirmedEvent capturedEvent = eventCaptor.getValue();

        assertSame(user, capturedEvent.getUser());

    }


}
