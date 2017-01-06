package com.radioteria.db.tests.dao;

import com.radioteria.db.dao.api.ChannelDao;
import com.radioteria.db.dao.api.UserDao;
import com.radioteria.db.entities.Channel;
import com.radioteria.db.entities.User;
import com.radioteria.db.tests.utils.EntityFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:db-context.xml")
@ActiveProfiles("test")
public class ChannelDaoTest {

    @Resource
    ChannelDao channelDao;

    @Resource
    UserDao userDao;

    private EntityFactory entityFactory = new EntityFactory();

    private User user;

    @Before
    public void initUser() {
        user = entityFactory.createUser("foo@bar.com");
        userDao.persist(user);
        userDao.flush();
    }

    @Test
    @Transactional
    public void testAddChannelToRepository() {
        Channel channel1 = entityFactory.createChannel("Channel 1");
        Channel channel2 = entityFactory.createChannel("Channel 2");

        user.addChannel(channel1);
        user.addChannel(channel2);

        userDao.flush();

        List<Channel> channels = user.getChannels();

        assertEquals(2, channels.size());

        user.getChannels().remove(channel1);
        userDao.flush();

        channel2.setDescription("Updated description.");
        userDao.flush();

        user.getChannels().clear();
        userDao.flush();

        assertEquals(0, channels.size());
        userDao.flush();
    }

}
