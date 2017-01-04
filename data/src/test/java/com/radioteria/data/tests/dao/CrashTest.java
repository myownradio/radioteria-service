package com.radioteria.data.tests.dao;

import com.radioteria.data.dao.api.ChannelDao;
import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.Track;
import com.radioteria.data.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:profiles/data-test-context.xml")
public class CrashTest {

    private List<User> users = new ArrayList<>();

    @Resource
    private UserDao userDao;

    @Resource
    private ChannelDao channelDao;

    @Test
    @Transactional
    public void go() {
        createUsers(5, user -> {
            System.err.println(user.getName());
            createChannels(user, 200, channel ->
                    createTracks(channel, 500, track -> {

                    })
            );
        });
    }

    private void createTracks(Channel channel, int count, Consumer<Track> consumer) {
        for (int i = 0; i < count; i ++) {
            Track track = new Track();

            track.setDuration(new Random().nextLong());
            track.setTitle("New Track #" + i + 1);
            track.setArtist("New Artist #" + i + 1);

            channel.addTrack(track);
            consumer.accept(track);
        }
    }

    private void createChannels(User user, int count, Consumer<Channel> consumer) {
        for (int i = 0; i < count; i ++) {
            Channel channel = new Channel();
            channel.setName("Channel #" + i + 1);
            channel.setDescription("");
            user.addChannel(channel);

            channelDao.persist(channel);
            consumer.accept(channel);
        }
    }

    private void createUsers(int count, Consumer<User> consumer) {
        for (int i = 0; i < count; i ++) {
            User user = new User();
            user.setName("User #" + i + 1);
            user.setEmail("user" + i + "@mail.com");
            user.setPassword("123");
            users.add(user);

            userDao.persist(user);
            consumer.accept(user);
        }
    }
}
