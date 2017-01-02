package com.radioteria.data.tests.utils;

import com.radioteria.data.entities.*;
import com.radioteria.data.enumerations.UserState;
import org.springframework.stereotype.Repository;

@Repository
public class EntityFactory {

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
        channel.setDescription("Default Description");
        return channel;
    }

    public Track createTrack(String name) {
        Track track = new Track();
        track.setTitle(name);
        return track;
    }

    public File createFile() {
        File file = new File();
        file.setName("file.bin");
        return file;
    }

}
