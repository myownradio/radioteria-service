package com.radioteria.test.business.services.remindPasswordService;

import com.radioteria.backing.template.TemplateWithContext;
import com.radioteria.db.entities.User;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RemindPasswordLetterTest extends AbstractRemindPasswordServiceTest {

    @Test
    public void testRemindPasswordLetter() {

        User user = new User();
        user.setEmail("foo@bar.com");
        user.setPassword("password");

        remindPasswordService.sendRemindPasswordLetter(user);

        verifyEmailServiceCall();

    }

    private void verifyEmailServiceCall() {

        ArgumentCaptor<String> to = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TemplateWithContext> templateWithContext = ArgumentCaptor.forClass(TemplateWithContext.class);

        verify(emailService, times(1))
                .sendTemplateBasedLetter(to.capture(), anyString(), templateWithContext.capture());

        Map<String, Object> context = templateWithContext.getValue().getTemplateContext();

        assertEquals("foo@bar.com", to.getValue());

        assertThat(context.get("user"), instanceOf(User.class));
        assertThat(context.get("code"), instanceOf(String.class));

    }

}
