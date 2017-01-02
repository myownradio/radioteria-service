package com.radioteria.data.tests.dao;

import com.radioteria.data.dao.api.*;
import com.radioteria.data.entities.*;
import com.radioteria.data.enumerations.UserState;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:data-context-test.xml")
public class FullDaoTest {
    private UserDao userDao;
    private ChannelDao channelDao;
    private TrackDao trackDao;
    private FileDao fileDao;
    private ContentDao contentDao;

    @Autowired
    public void autowire(UserDao userDao, ChannelDao channelDao, TrackDao trackDao, FileDao fileDao, ContentDao contentDao) {
        this.userDao = userDao;
        this.channelDao = channelDao;
        this.trackDao = trackDao;
        this.fileDao = fileDao;
        this.contentDao = contentDao;
    }

    @After
    public void flush() {
        userDao.flush();
        channelDao.flush();
        trackDao.flush();
        fileDao.flush();
        contentDao.flush();
    }

    @Test
    @Transactional
    public void testCreateUser() {
        User user = createUser();

        assertTrue(isPersist(user));
        assertThat(userDao.list().get(0), equalTo(user));
    }

    @Test
    @Transactional
    public void testCreateChannel() {
        User user = createUser();
        Channel channel = createChannelForUser(user);

        assertTrue(isPersist(channel));
        assertThat(channelDao.list().size(), equalTo(1));
        assertThat(channel.getUser(), equalTo(user));
        assertThat(user.getChannels().get(0), equalTo(channel));
    }

    @Test
    @Transactional
    public void testCreateTrack() {
        User user = createUser();
        Channel channel = createChannelForUser(user);
        Track track = createTrackInChannel(channel);

        assertThat(trackDao.list().size(), equalTo(1));
        assertThat(track.getChannel(), equalTo(channel));
        assertThat(channel.getTracks().get(0), equalTo(track));
    }

    @Test
    @Transactional
    public void testCreateContent() {
        Content content = createContent();

        assertThat(contentDao.list().get(0), equalTo(content));
    }

    @Test
    @Transactional
    public void testCreateFile() {
        Content content = createContent();
        File file = createFileOfContent(content);

        assertThat(fileDao.list().get(0), equalTo(file));
        assertThat(file.getContent(), equalTo(content));
    }

    @Test
    @Transactional
    public void testCreateAvatar() {
        User user = createUser();
        Content avatarContent = createContent();
        File avatarFile = createFileOfContent(avatarContent);
        user.setAvatarFile(avatarFile);

        assertThat(user.getAvatarFile(), equalTo(avatarFile));
    }

    @Test
    @Transactional
    public void testCreateArtwork() {
        User user = createUser();
        Channel channel = createChannelForUser(user);
        Content artworkContent = createContent();
        File artworkFile = createFileOfContent(artworkContent);

        channel.setArtworkFile(artworkFile);

        assertThat(channel.getArtworkFile(), equalTo(artworkFile));
    }

    private User createUser() {
        User user = new User();
        user.setEmail("user@mail.com");
        user.setName("Test User");
        user.setPassword("Password");
        user.setState(UserState.ACTIVE);
        userDao.persist(user);
        return user;
    }

    private Channel createChannelForUser(User user) {
        Channel channel = new Channel();
        channel.setName("Test Channel");
        channel.setDescription("Channel Description.");
        user.addChannel(channel);
        channelDao.persist(channel);
        return channel;
    }

    private Track createTrackInChannel(Channel channel) {
        Track track = new Track();
        track.setTitle("Track Title");
        track.setArtist("Track Artist");
        track.setDuration(1_000_000L);
        channel.addTrack(track);
        trackDao.persist(track);
        return track;
    }

    private Content createContent() {
        Content content = new Content();
        content.setHash("hash");
        content.setLength(512L);
        content.setType("application/octet-stream");
        contentDao.persist(content);
        return content;
    }

    private File createFileOfContent(Content content) {
        File file = new File();
        file.setName("file.txt");
        file.setContent(content);
        fileDao.persist(file);
        return file;
    }

    private boolean isPersist(BaseEntity baseEntity) {
        return baseEntity.getId() != null;
    }
}
