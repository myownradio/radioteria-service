package com.radioteria.data.dao.tests;

import com.radioteria.data.dao.api.ChannelDao;
import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Set;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/test-context.xml")
public class ChannelDaoTest {

    @Resource
    ChannelDao channelDao;

    @Resource
    UserDao userDao;

    private User user;

    @Before
    public void initUser() {
        user = new User("foo@bar.com", null, "Foo Bar");
        userDao.save(user);
        userDao.flush();
    }

    @Test
    public void testAddFreeChannelToRepository() {
        Channel channel = new Channel("Foo Bar", "");

        channelDao.save(channel);
        channelDao.flush();

        assertNotNull(channel.getId());
    }

    @Test
    public void testAddChannelToRepository() {
        Channel channel1 = new Channel("Channel 1", "");
        Channel channel2 = new Channel("Channel 2", "");

        user.addChannel(channel1);
        user.addChannel(channel2);

        userDao.save(user);
        userDao.flush();

        Set<Channel> channels = user.getChannels();

        assertEquals(2, channels.size());

        user.getChannels().remove(channel1);

        assertEquals(1, channels.size());

        channel2.setDescription("Updated description.");

        userDao.save(user);
        userDao.flush();

        user.getChannels().clear();

        userDao.save(user);
        userDao.flush();

        assertEquals(0, channels.size());
    }

}
