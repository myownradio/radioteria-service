package com.radioteria.data.tests.dao;

import com.radioteria.data.dao.api.ChannelDao;
import com.radioteria.data.dao.api.FileDao;
import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.File;
import com.radioteria.data.entities.User;
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
public class FileDaoTest {

    @Resource
    private FileDao fileDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ChannelDao channelDao;

    @Test
    public void testFileCreate() {
        File file = new File();
        fileDao.save(file);

        assertEquals(1, fileDao.list().size());
    }


    public void changeFileLinks() {
        File file = new File();
        fileDao.save(file);

        fileDao.increaseLinksCount(file);
        assertEquals(new Long(1), file.getLinksCount());

        fileDao.increaseLinksCount(file);
        assertEquals(new Long(2), file.getLinksCount());

        fileDao.decreaseLinksCount(file);
        assertEquals(new Long(1), file.getLinksCount());

        fileDao.decreaseLinksCount(file);
        assertEquals(new Long(0), file.getLinksCount());
    }

    @Test
    public void testMakeUserAvatar() {
        File avatar = new File();
        fileDao.save(avatar);

        User user = new User("foo@example.com", "", "");
        userDao.save(user);

        user.setAvatarFile(avatar);
        userDao.save(user);

        userDao.flush();

        assertSame(user.getAvatarFile(), avatar);
    }

    @Test
    public void testChannelArtwork() {
        User owner = new User("foo@example.com", "", "");
        userDao.save(owner);

        File artwork = new File();
        fileDao.save(artwork);

        Channel channel = new Channel("Channel 1", "");
        owner.addChannel(channel);
        channelDao.save(channel);

        channel.setArtworkFile(artwork);
        channelDao.save(channel);

        channelDao.flush();

        assertSame(channel.getArtworkFile(), artwork);
    }
}
