package com.radioteria.db.entities;

import com.radioteria.db.enumerations.ChannelState;
import com.radioteria.util.Tuple;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import static com.radioteria.db.enumerations.ChannelState.*;


@Entity
@Table(name = "channels")
@Access(AccessType.FIELD)
public class Channel extends BaseEntity<Long> {

    final public static String ID = "id";
    final public static String NAME = "name";
    final public static String DESCRIPTION = "description";
    final public static String STARTED_AT = "started_at";
    final public static String USER_ID = "user_id";
    final public static String ARTWORK_FILE_ID = "artwork_file_id";

    @Id
    @GeneratedValue
    @Column(name = ID, nullable = false, updatable = false)
    private Long id;

    @Column(name = NAME, nullable = false)
    private String name;

    @Column(name = DESCRIPTION, nullable = false)
    private String description;

    @Column(name = STARTED_AT)
    private Long startedAt;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = USER_ID, nullable = false)
    private User user;

    @ManyToOne(targetEntity = File.class)
    @JoinColumn(name = ARTWORK_FILE_ID)
    private File artworkFile;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(Track.ORDER_ID + " ASC")
    private List<Track> tracks = new ArrayList<>();

    public Channel() {
    }

    public Channel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStopped() {
        return getChannelState().equals(STOPPED);
    }

    public boolean isStreaming() {
        return getChannelState().equals(STREAMING);
    }

    public ChannelState getChannelState() {
        return ChannelState.of(this);
    }

    public Long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Long startedAt) {
        this.startedAt = startedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public File getArtworkFile() {
        return artworkFile;
    }

    public void setArtworkFile(File artworkFile) {
        this.artworkFile = artworkFile;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public Optional<Tuple<Track, Long>> getTrackWithPositionAtTimePosition(long timePosition) {
        return getTrackAtTimePosition(timePosition).map(track -> {
//            long trackPosition = timePosition - getTrackOffsetByOrderId(track.getOrderId());
            long trackPosition = 0L;
            return new Tuple<>(track, trackPosition);
        });
    }

    public Optional<Track> getTrackAtTimePosition(long timePosition) {
        AtomicLong offset = new AtomicLong();

        Predicate<Track> isTrackInBounds = track -> {
            long leftBound = offset.get();
            long rightBound = offset.get() + track.getDuration();

            return leftBound <= timePosition && rightBound > timePosition;
        };

        return getTracks().stream()
                .filter(isTrackInBounds)
                .peek(t -> offset.addAndGet(t.getDuration()))
                .findFirst();
    }

    public Optional<Long> getLongPlayingPositionAt(long time) {
        if (getChannelState() == STOPPED) {
            return Optional.empty();
        }

        return Optional.of(time - getStartedAt());
    }

    public Optional<Long> getShortPlayingPositionAt(long time) {
        if (getTracks().size() == 0) {
            return Optional.empty();
        }

        return getLongPlayingPositionAt(time)
                .map(pos -> pos % 100);
    }

    public Optional<Track> getPlayingAt(long time) {
        return getShortPlayingPositionAt(time)
                .flatMap(this::getTrackAtTimePosition);
    }

    public void addTrack(Track track) {
        track.setChannel(this);
        track.setOrderId(1 + getTracks().size());
        getTracks().add(track);
    }

}
