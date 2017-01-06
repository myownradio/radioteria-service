package com.radioteria.test.business.services.remindPasswordService;

import com.radioteria.business.services.misc.PasswordRecoveryCode;
import com.radioteria.business.services.user.exceptions.RemindPasswordServiceException;
import com.radioteria.db.entities.User;
import org.junit.Test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class VerifyPasswordRecoveryCodeTest extends AbstractRemindPasswordServiceTest {
    @Test
    public void justVerifyCode() {
        User user = userDao.findByEmail("foo@bar.com").get();
        String code = new PasswordRecoveryCode(user, System.currentTimeMillis() + 30_000L).encode();

        remindPasswordService.verifyPasswordRecoveryCode(code);
    }

    @Test
    public void changePassword() {
        User user = userDao.findByEmail("foo@bar.com").get();
        String code = new PasswordRecoveryCode(user, System.currentTimeMillis() + 30_000L).encode();

        remindPasswordService.changePasswordUsingRecoveryCode(code, "new password");

        verify(userService, times(1)).changePassword(user, "new password");
    }

    @Test(expected = RemindPasswordServiceException.class)
    public void verifyBrokenCode() {
        remindPasswordService.verifyPasswordRecoveryCode("broken code");
    }

    @Test(expected = RemindPasswordServiceException.class)
    public void verifyStaleCode() {
        User user = userDao.findByEmail("foo@bar.com").get();
        String code = new PasswordRecoveryCode(user, System.currentTimeMillis() - 30_000L).encode();

        remindPasswordService.verifyPasswordRecoveryCode(code);
    }

    @Test(expected = RemindPasswordServiceException.class)
    public void verifyUpdatedUser() {
        User user = userDao.findByEmail("foo@bar.com").get();
        String code = new PasswordRecoveryCode(user, System.currentTimeMillis() + 30_000L).encode();

        user.setPassword("other password");

        remindPasswordService.verifyPasswordRecoveryCode(code);
    }
}
