package com.radioteria.business.services.channels.api;

import com.radioteria.business.services.AccessControlled;
import com.radioteria.data.entities.Track;
import com.radioteria.data.entities.User;

public interface ChannelControl extends AccessControlled<User> {
    void start();
    void start(Track track);
    void stop();
    void rewind();
    void next();
    void previous();
}
