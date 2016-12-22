package com.radioteria.data.tests.dao;

import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.User;
import com.radioteria.data.tests.utils.TestEntityFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/test-data-context.xml")
public class UserDaoTest {

    @Resource
    UserDao userDao;

    @Resource
    TestEntityFactory entityFactory;

    @Test
    @Transactional
    public void testAddUser() {
        User newUser = entityFactory.createUser("foo@exambple.com");

        userDao.persist(newUser);

        assertEquals(newUser.getId(), userDao.list().get(0).getId());
    }

    @Test
    @Transactional
    public void testDeleteUserFromRepo() {
        User newUser = entityFactory.createUser("abc@foo.com");

        userDao.persist(newUser);
        userDao.delete(newUser);

        assertEquals(0, userDao.list().size());
    }

    @Test
    @Transactional
    public void testFindUserByEmail() {
        userDao.persist(entityFactory.createUser("abc@foo.com"));

        User foundUser = userDao.findByEmail("abc@foo.com");

        assertEquals("abc@foo.com", foundUser.getEmail());
    }

    @Test
    @Transactional
    public void testUpdateUserDetails() {
        User user = entityFactory.createUser("foo@bar.com");
        userDao.persist(user);

        assertEquals("Default Name", userDao.list().get(0).getName());

        user.setName("Foo Bar");

        assertEquals("Foo Bar", userDao.list().get(0).getName());
    }

    @Test(expected = PersistenceException.class)
    @Transactional
    public void testUserEmailConstraint() {
        userDao.persist(entityFactory.createUser("foo@bar.com"));
        userDao.persist(entityFactory.createUser("foo@bar.com"));
        userDao.flush();
    }

    @Test
    @Transactional
    public void testNotExistentUserReturnsNull() {
        assertNull(userDao.find(1000L));
    }

}
