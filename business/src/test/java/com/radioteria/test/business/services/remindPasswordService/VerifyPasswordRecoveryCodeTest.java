package com.radioteria.test.business.services.remindPasswordService;

import static org.mockito.Mockito.verify;

public class VerifyPasswordRecoveryCodeTest extends AbstractPasswordRecoveryServiceTest {
//    @Test
//    public void verifyCode() {
//        User user = userDao.findByEmail("foo@bar.com").get();
//        String code = new PasswordRecoveryCode(user, System.currentTimeMillis() + 30_000L).encode();
//
//        passwordRecoveryService.verifyPasswordRecoveryCode(code);
//    }
//
//    @Test(expected = PasswordRecoveryServiceException.class)
//    public void verifyBrokenCode() {
//        passwordRecoveryService.verifyPasswordRecoveryCode("broken code");
//    }
//
//    @Test(expected = PasswordRecoveryServiceException.class)
//    public void verifyStaleCode() {
//        User user = userDao.findByEmail("foo@bar.com").get();
//        String code = new PasswordRecoveryCode(user, System.currentTimeMillis() - 30_000L).encode();
//
//        passwordRecoveryService.verifyPasswordRecoveryCode(code);
//    }
//
//    @Test(expected = PasswordRecoveryServiceException.class)
//    public void verifyUpdatedUser() {
//        User user = userDao.findByEmail("foo@bar.com").get();
//        String code = new PasswordRecoveryCode(user, System.currentTimeMillis() + 30_000L).encode();
//
//        user.setPassword("other password");
//
//        passwordRecoveryService.verifyPasswordRecoveryCode(code);
//    }
//
//    @Test(expected = PasswordRecoveryServiceException.class)
//    public void verifyNonExistentUser() {
//        User user = new User();
//        user.setEmail("deleted@mail.com");
//        String code = new PasswordRecoveryCode(user, System.currentTimeMillis() + 30_000L).encode();
//
//        passwordRecoveryService.verifyPasswordRecoveryCode(code);
//    }
//
//
//    @Test
//    public void changePasswordStaleCode() {
//        User user = userDao.findByEmail("foo@bar.com").get();
//        String code = new PasswordRecoveryCode(user, System.currentTimeMillis() - 30_000L).encode();
//
//        try {
//            passwordRecoveryService.changePasswordUsingRecoveryCode(code, "new password");
//        } catch (PasswordRecoveryServiceException e) {
//            /* NOP */
//        }
//
//        verify(userService, times(0)).changePassword(user, "new password");
//    }
//
//    @Test
//    public void changePasswordUpdatedUser() {
//        User user = userDao.findByEmail("foo@bar.com").get();
//        String code = new PasswordRecoveryCode(user, System.currentTimeMillis() + 30_000L).encode();
//
//        user.setPassword("other password");
//
//        try {
//            passwordRecoveryService.changePasswordUsingRecoveryCode(code, "new password");
//        } catch (PasswordRecoveryServiceException e) {
//            /* NOP */
//        }
//
//        verify(userService, times(0)).changePassword(user, "new password");
//    }
//
//
//    @Test
//    public void changePasswordForNonExistentUser() {
//        User user = new User();
//        user.setEmail("non@m.com");
//        String code = new PasswordRecoveryCode(user, System.currentTimeMillis() + 30_000L).encode();
//
//        try {
//            passwordRecoveryService.changePasswordUsingRecoveryCode(code, "new password");
//            Assert.fail("Expected exception.");
//        } catch (PasswordRecoveryServiceException e) {
//            /* NOP */
//        }
//
//        verify(userService, times(0)).changePassword(user, "new password");
//    }
}
