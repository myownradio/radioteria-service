package com.radioteria.test.business.services.remindPasswordService;

public class RemindPasswordLetterTest extends AbstractPasswordRecoveryServiceTest {
//    @Test
//    public void testRemindPasswordLetterActiveUser() {
//        User user = createUserWithGivenState(UserState.ACTIVE);
//
//        passwordRecoveryService.sendPasswordRecoveryLetter(user);
//
//        verifyEmailServiceCall(user);
//        verifyTemplateServiceCall(user);
//    }
//
//    @Test
//    public void testRemindPasswordLetterInactiveUser() {
//        User user = createUserWithGivenState(UserState.INACTIVE);
//
//        passwordRecoveryService.sendPasswordRecoveryLetter(user);
//
//        verifyEmailServiceCall(user);
//        verifyTemplateServiceCall(user);
//    }
//
//    @Test(expected = PasswordRecoveryServiceException.class)
//    public void testRemindPasswordLetterDeletedUser() {
//        User user = createUserWithGivenState(UserState.DELETED);
//
//        passwordRecoveryService.sendPasswordRecoveryLetter(user);
//    }
//
//    private User createUserWithGivenState(UserState userState) {
//        User user = new User();
//        user.setId(1L);
//        user.setEmail("foo@bar.com");
//        user.setName("Foo");
//        user.setPassword("i_don't_remember");
//        user.setState(userState);
//        return user;
//    }
//
//    private void verifyEmailServiceCall(User user) {
//        ArgumentCaptor<String> to = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> letterBody = ArgumentCaptor.forClass(String.class);
//
//        verify(emailService, times(1))
//                .send(to.capture(), anyString(), letterBody.capture());
//
//        assertThat(to.getValue(), equalTo(user.getEmail()));
//    }
//
//    private void verifyTemplateServiceCall(User user) {
//        ArgumentCaptor<String> templateCaptor = ArgumentCaptor.forClass(String.class);
//        Class<HashMap<String, Object>> hashMapClass = (Class<HashMap<String, Object>>) (Class) HashMap.class;
//        ArgumentCaptor<HashMap<String, Object>> contextCaptor = ArgumentCaptor.forClass(hashMapClass);
//
//        verify(templateService, times(1)).render(templateCaptor.capture(), contextCaptor.capture());
//
//        Map<String, Object> context = contextCaptor.getValue();
//        String template = templateCaptor.getValue();
//
//        assertThat(context.get("user"), equalTo(user));
//        assertTrue(context.get("code") instanceof String);
//        assertThat(template, equalTo(PasswordRecoveryServiceImpl.PASSWORD_RECOVERY_LETTER_TEMPLATE));
//    }
}
