package com.radioteria.data.tests.utils;

import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.File;
import com.radioteria.data.entities.User;
import com.radioteria.data.enumerations.ChannelState;
import com.radioteria.data.enumerations.UserState;
import org.springframework.stereotype.Repository;

@Repository
public class TestEntityFactory {

    public User createUser(String email) {
        User user = new User();

        user.setEmail(email);
        user.setName("Default Name");
        user.setPassword("");
        user.setState(UserState.INACTIVE);

        return user;
    }

    public Channel createChannel(String name) {
        Channel channel = new Channel();

        channel.setName(name);
        channel.setChannelState(ChannelState.STOPPED);
        channel.setDescription("Default Description");

        return channel;
    }

    public File createFile() {
        File file = new File();

        file.setLinksCount(0L);

        return file;
    }

}
