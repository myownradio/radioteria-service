package com.radioteria.data.dao.tests;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/data-context.xml")
@Transactional
public class UserDaoTest {

    @Resource
    UserDao userDao;

    @Test
    public void testAddUserToRepository() {
        User newUser = new User("foo@exambple.com", "password", "Foo Bar");

        assertNull(newUser.getId());

        userDao.persist(newUser);

        Long newId = newUser.getId();

        assertNotNull(newId);

        User fetchedUser = userDao.find(newId);

        assertNotNull(fetchedUser);

        assertEquals(newId, fetchedUser.getId());
    }

    @Test
    public void testDeleteUserFromRepository() {
        assertEquals(0, userDao.list().size());

        User newUser = new User("abc@foo.com", "pass", "Abc");
        userDao.persist(newUser);

        assertNotNull(newUser.getId());
        assertEquals(1, userDao.list().size());

        userDao.delete(newUser);

        assertEquals(0, userDao.list().size());
    }

    @Test
    public void testFindUserByEmail() {
        userDao.persist(new User("abc@foo.com", "pass", "Abc"));

        User foundUser = userDao.findByEmail("abc@foo.com");

        assertNotNull(foundUser);
        assertEquals("abc@foo.com", foundUser.getEmail());
    }

    @Test
    public void testUpdateUserDetails() {
        User user = new User("foo@bar.com", "baz", "");

        userDao.persist(user);

        assertEquals("", user.getName());

        user.setName("Foo Bar");

        userDao.update(user);

        User updatedUser = userDao.find(user.getId());

        assertEquals("Foo Bar", updatedUser.getName());
    }

    @Test(expected = PersistenceException.class)
    public void testUserEmailConstraint() {
        userDao.persist(new User("foo@bar.com", "baz", "Foo Bar"));
        userDao.persist(new User("foo@bar.com", "baz2", "Foo Bar 2"));
        userDao.flush();
    }

    @Test
    public void testEntityCaching() {
        userDao.persist(new User("foo@bar.com", "baz", "Foo Bar"));

        User one = userDao.findByEmail("foo@bar.com");
        User other = userDao.findByEmail("foo@bar.com");

        assertSame(one, other);
    }
}
