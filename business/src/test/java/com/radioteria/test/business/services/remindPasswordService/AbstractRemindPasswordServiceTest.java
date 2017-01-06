package com.radioteria.test.business.services.remindPasswordService;

import com.radioteria.business.services.user.api.RemindPasswordService;
import com.radioteria.business.services.user.impl.RemindPasswordServiceImpl;
import com.radioteria.db.dao.api.UserDao;
import com.radioteria.db.entities.User;
import com.radioteria.db.enumerations.UserState;
import com.radioteria.support.services.mail.EmailService;
import com.radioteria.support.template.TemplateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
abstract public class AbstractRemindPasswordServiceTest {
    @Mock
    protected UserDao userDao;

    @Mock
    protected EmailService emailService;

    @Mock
    protected TemplateService templateService;


    protected RemindPasswordService remindPasswordService;

    @Before
    public void setup() {
        remindPasswordService = new RemindPasswordServiceImpl(userDao, emailService, templateService);
    }
}
