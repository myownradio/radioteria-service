package com.radioteria.test.business.services.remindPasswordService;

import com.radioteria.db.enumerations.UserState;
import com.radioteria.support.template.TemplateWithContext;
import com.radioteria.db.entities.User;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RemindPasswordLetterTest extends AbstractRemindPasswordServiceTest {
    @Test
    public void testRemindPasswordLetter() {
        User user = new User();
        user.setId(1L);
        user.setEmail("foo@bar.com");
        user.setName("Foo");
        user.setPassword("i_don't_remember");
        user.setState(UserState.ACTIVE);

        remindPasswordService.sendRemindPasswordLetter(user);

        verifyEmailServiceCall(user);
        verifyTemplateServiceCall(user);
    }

    private void verifyEmailServiceCall(User user) {
        ArgumentCaptor<String> to = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> letterBody = ArgumentCaptor.forClass(String.class);

        verify(emailService, times(1))
                .send(to.capture(), anyString(), letterBody.capture());

        assertThat(to.getValue(), equalTo(user.getEmail()));
    }

    private void verifyTemplateServiceCall(User user) {
        ArgumentCaptor<String> templateCaptor = ArgumentCaptor.forClass(String.class);
        Class<HashMap<String, Object>> hashMapClass = (Class<HashMap<String, Object>>) (Class) HashMap.class;
        ArgumentCaptor<HashMap<String, Object>> contextCaptor = ArgumentCaptor.forClass(hashMapClass);

        verify(templateService, times(1)).render(templateCaptor.capture(), contextCaptor.capture());

        Map<String, Object> context = contextCaptor.getValue();
        String template = templateCaptor.getValue();

        assertThat(context.get("user"), equalTo(user));
        assertTrue(context.get("code") instanceof  String);
        assertThat(template, equalTo("email.remind-password"));
    }
}
