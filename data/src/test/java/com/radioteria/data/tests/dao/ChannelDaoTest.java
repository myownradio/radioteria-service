package com.radioteria.data.tests.dao;

import com.radioteria.data.dao.api.ChannelDao;
import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.User;
import com.radioteria.data.tests.utils.TestEntityFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/test-context.xml")
public class ChannelDaoTest {

    @Resource
    ChannelDao channelDao;

    @Resource
    UserDao userDao;

    @Resource
    TestEntityFactory entityFactory;

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
