package com.radioteria.business.services.user.api;

import com.radioteria.db.entities.Track;

import java.util.List;
import java.util.Optional;

public interface PlaylistService {
    List<Track> getTracks();

    boolean isEmpty();

    Optional<Track> getFirstTrack();

    Optional<Track> getLastTrack();
}
