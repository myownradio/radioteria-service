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
        User newUser = new User("foo@exambple.com");
        assertNull(newUser.getId());

        userDao.persist(newUser);

        Long newId = newUser.getId();
        assertNotNull(newId);

        userDao.flush();
        userDao.clear();

        User fetchedUser = userDao.find(newId);
        assertNotNull(fetchedUser);
        assertEquals(newId, fetchedUser.getId());
    }

    @Test
    public void testDeleteUserFromRepo() {
        assertEquals(0, userDao.list().size());

        User newUser = new User("abc@foo.com");
        userDao.persist(newUser);

        assertNotNull(newUser.getId());
        assertEquals(1, userDao.list().size());

        userDao.delete(newUser);

        assertEquals(0, userDao.list().size());
    }

    @Test
    public void testDeleteUserFromRepoByKey() {
        assertEquals(0, userDao.list().size());

        User user = new User("abc@foo.com");
        userDao.persist(user);
        userDao.flush();

        assertEquals(1, userDao.list().size());

        userDao.deleteByKey(user.getId());

        assertEquals(0, userDao.list().size());
    }


    @Test
    public void testFindUserByEmail() {
        userDao.persist(new User("abc@foo.com"));

        User foundUser = userDao.findByEmail("abc@foo.com");

        assertNotNull(foundUser);
        assertEquals("abc@foo.com", foundUser.getEmail());
    }

    @Test
    public void testUpdateUserDetails() {
        User user = new User("foo@bar.com");

        userDao.persist(user);

        assertEquals("", user.getName());

        user.setName("Foo Bar");

        User updatedUser = userDao.find(user.getId());

        assertEquals("Foo Bar", updatedUser.getName());

        userDao.flush();
    }

    @Test(expected = PersistenceException.class)
    public void testUserEmailConstraint() {
        userDao.persist(new User("foo@bar.com"));
        userDao.persist(new User("foo@bar.com"));
        userDao.flush();
    }

    @Test
    public void testNotExistentUserReturnsNull() {
        assertNull(userDao.find(1000L));
    }

}
