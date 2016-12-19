package com.radioteria.data.dao.tests;

import com.radioteria.data.dao.api.ChannelDao;
import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.Channel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/test-context.xml")
public class ChannelDaoTest {

    @Resource
    ChannelDao channelDao;

    @Resource
    UserDao userDao;

    @Test
    public void testCreateChannel() {
        Channel channel = new Channel("Foo Bar", null);

        channelDao.saveOrUpdate(channel);
        channelDao.flush();

        assertNotNull(channel.getId());
    }

}
