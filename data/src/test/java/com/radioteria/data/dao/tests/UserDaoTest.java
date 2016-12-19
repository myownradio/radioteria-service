package com.radioteria.data.dao.tests;

import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;

import java.util.ArrayList;
import java.util.List;

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

        userDao.saveOrUpdate(newUser);

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
        userDao.saveOrUpdate(newUser);

        assertNotNull(newUser.getId());
        assertEquals(1, userDao.list().size());

        userDao.delete(newUser);

        assertEquals(0, userDao.list().size());
    }

    @Test
    public void testFindUserByEmail() {
        userDao.saveOrUpdate(new User("abc@foo.com", "pass", "Abc"));

        User foundUser = userDao.findByEmail("abc@foo.com");

        assertNotNull(foundUser);
        assertEquals("abc@foo.com", foundUser.getEmail());
    }

    @Test
    public void testUpdateUserDetails() {
        User user = new User("foo@bar.com", "baz", "");

        userDao.saveOrUpdate(user);

        assertEquals("", user.getName());

        user.setName("Foo Bar");

        userDao.saveOrUpdate(user);

        User updatedUser = userDao.find(user.getId());

        assertEquals("Foo Bar", updatedUser.getName());
    }

    @Test(expected = PersistenceException.class)
    public void testUserEmailConstraint() {
        userDao.saveOrUpdate(new User("foo@bar.com", "baz", "Foo Bar"));
        userDao.saveOrUpdate(new User("foo@bar.com", "baz2", "Foo Bar 2"));
        userDao.flush();
    }

    @Test
    public void testUserChannelsList() {
        User user = new User("foo@bar.com", "", "");

        user.setChannels(new ArrayList<Channel>() {{
            this.add(new Channel("First Channel", user));
            this.add(new Channel("Second Channel", user));
        }});

        userDao.saveOrUpdate(user);

        List<Channel> channels = userDao.find(user.getId()).getChannels();

        assertEquals(2, channels.size());
    }
}
