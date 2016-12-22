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
    @Transactional
    public void testFileCreate() {
        File file = new File();
        fileDao.persist(file);

        assertEquals(file.getId(), fileDao.list().get(0).getId());
    }

    @Test
    @Transactional
    public void changeFileLinks() {
        File file = new File();
        fileDao.persist(file);
        assertEquals(new Long(0), file.getLinksCount());

        fileDao.increaseLinksCount(file);
        assertEquals(new Long(1), file.getLinksCount());

        fileDao.increaseLinksCount(file);
        assertEquals(new Long(2), file.getLinksCount());

        fileDao.decreaseLinksCount(file);
        assertEquals(new Long(1), file.getLinksCount());

        fileDao.decreaseLinksCount(file);
        assertEquals(new Long(0), file.getLinksCount());

        fileDao.flush();
    }

    @Test
    @Transactional
    public void testMakeUserAvatar() {
        File avatar = new File();
        fileDao.persist(avatar);

        User user = new User("foo@example.com");
        userDao.persist(user);

        user.setAvatarFile(avatar);

        assertSame(user.getAvatarFile().getId(), avatar.getId());

        userDao.flush();
    }

    @Test
    @Transactional
    public void testChannelArtwork() {
        User owner = new User("foo@example.com");
        userDao.persist(owner);

        File artwork = new File();
        fileDao.persist(artwork);

        Channel channel = new Channel("Channel 1", "");
        channelDao.persist(channel);

        owner.addChannel(channel);

        channel.setArtworkFile(artwork);

        assertSame(channel.getArtworkFile(), artwork);

        channelDao.flush();
    }
}
