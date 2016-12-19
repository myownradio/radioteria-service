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

import java.util.List;

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
        userDao.saveOrUpdate(user);
    }

    @Test
    public void testAddFreeChannelToRepository() {
        Channel channel = new Channel("Foo Bar", "", null);

        channelDao.saveOrUpdate(channel);
        channelDao.flush();

        assertNotNull(channel.getId());
    }

    @Test
    public void testAddChannelToRepository() {
        Channel channel1 = new Channel("Channel 1", "", user);
        Channel channel2 = new Channel("Channel 2", "", user);

        channelDao.saveOrUpdate(channel1);
        channelDao.saveOrUpdate(channel2);

        List<Channel> channels = user.getChannels();

        assertNotNull(channels);
        assertEquals(2, channels.size());
    }

}
