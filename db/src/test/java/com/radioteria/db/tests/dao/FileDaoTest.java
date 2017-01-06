package com.radioteria.db.tests.dao;

import com.radioteria.db.dao.api.ChannelDao;
import com.radioteria.db.dao.api.FileDao;
import com.radioteria.db.dao.api.UserDao;
import com.radioteria.db.entities.Channel;
import com.radioteria.db.entities.File;
import com.radioteria.db.entities.User;
import com.radioteria.db.tests.utils.EntityFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:db-context.xml")
@ActiveProfiles("test")
public class FileDaoTest {

    @Resource
    private FileDao fileDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ChannelDao channelDao;

    private EntityFactory entityFactory = new EntityFactory();

    @Test
    @Transactional
    public void testFileCreate() {
        File file = entityFactory.createFile();
        fileDao.persist(file);

        assertEquals(file.getId(), fileDao.list().get(0).getId());
    }

    @Test
    @Transactional
    public void changeFileLinks() {

    }

    @Test
    @Transactional
    public void testMakeUserAvatar() {
        File avatar = entityFactory.createFile();
         fileDao.persist(avatar);

        User user = entityFactory.createUser("foo@example.com");
        userDao.persist(user);

        user.setAvatarFile(avatar);

        assertSame(user.getAvatarFile().getId(), avatar.getId());

        userDao.flush();
    }

    @Test
    @Transactional
    public void testChannelArtwork() {
        User owner = entityFactory.createUser("foo@example.com");
        userDao.persist(owner);

        File artwork = entityFactory.createFile();
        fileDao.persist(artwork);

        Channel channel = entityFactory.createChannel("Channel 1");

        owner.addChannel(channel);

        channel.setArtworkFile(artwork);

        assertEquals(channel.getArtworkFile().getId(), artwork.getId());

        channelDao.flush();
    }
}
