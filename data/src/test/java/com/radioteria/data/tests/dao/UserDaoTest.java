package com.radioteria.data.tests.dao;

import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/test-context.xml")
public class UserDaoTest {

    @Resource
    UserDao userDao;

    @Test
    public void testAddUserToRepository() {
        User newUser = new User("foo@exambple.com", "password", "Foo Bar");
        assertNull(newUser.getId());

        userDao.save(newUser);
        Long newId = newUser.getId();
        assertNotNull(newId);

        User fetchedUser = userDao.find(newId);
        assertNotNull(fetchedUser);
        assertEquals(newId, fetchedUser.getId());
    }

    @Test
    public void testDeleteUserFromRepo() {
        assertEquals(0, userDao.list().size());

        User newUser = new User("abc@foo.com", "pass", "Abc");
        userDao.save(newUser);

        assertNotNull(newUser.getId());
        assertEquals(1, userDao.list().size());

        userDao.delete(newUser);

        assertEquals(0, userDao.list().size());
    }

    @Test
    public void testDeleteUserFromRepoByKey() {
        assertEquals(0, userDao.list().size());

        User user = new User("abc@foo.com", "pass", "Abc");
        userDao.save(user);
        userDao.flush();

        assertEquals(1, userDao.list().size());

        userDao.deleteByKey(user.getId());

        assertEquals(0, userDao.list().size());
    }


    @Test
    public void testFindUserByEmail() {
        userDao.save(new User("abc@foo.com", "pass", "Abc"));

        User foundUser = userDao.findByEmail("abc@foo.com");

        assertNotNull(foundUser);
        assertEquals("abc@foo.com", foundUser.getEmail());
    }

    @Test
    public void testUpdateUserDetails() {
        User user = new User("foo@bar.com", "baz", "");

        userDao.save(user);

        assertEquals("", user.getName());

        user.setName("Foo Bar");

        User updatedUser = userDao.find(user.getId());

        assertEquals("Foo Bar", updatedUser.getName());

        userDao.flush();
    }

    @Test(expected = PersistenceException.class)
    public void testUserEmailConstraint() {
        userDao.save(new User("foo@bar.com", "baz", "Foo Bar"));
        userDao.save(new User("foo@bar.com", "baz2", "Foo Bar 2"));
        userDao.flush();
    }

    @Test
    public void testNotExistentUserReturnsNull() {
        assertNull(userDao.find(1000L));
    }

}
