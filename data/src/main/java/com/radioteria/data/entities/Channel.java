package com.radioteria.data.entities;

import com.radioteria.data.enumerations.ChannelState;
import com.radioteria.util.MathUtil;
import com.radioteria.util.OptionalUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.radioteria.util.FunctionalUtil.operator;
import static com.radioteria.util.FunctionalUtil.statefulPredicate;

@Entity
@Table(name = "channels")
@Access(AccessType.FIELD)
public class Channel extends Identifiable<Long> {

    final public static String ID = "id";
    final public static String NAME = "name";
    final public static String DESCRIPTION = "description";
    final public static String STATE = "state";
    final public static String STARTED_AT = "started_at";
    final public static String USER_ID = "user_id";
    final public static String ARTWORK_FILE_ID = "artwork_file_id";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = ID, nullable = false, updatable = false)
    private Long id;

    @Column(name = NAME, nullable = false)
    private String name;

    @Column(name = DESCRIPTION, nullable = false)
    private String description;

    @Enumerated
    @Column(name = STATE, nullable = false)
    private ChannelState channelState;

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

    public ChannelState getChannelState() {
        return channelState;
    }

    public void setChannelState(ChannelState channelState) {
        this.channelState = channelState;
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

    public Long getTracksDuration() {
        return getTracks().stream().mapToLong(Track::getDuration).sum();
    }

    public Optional<Track> getTrackByAtOrderId(Long orderId) {
        return getTracks()
                .stream()
                .filter(t -> t.getOrderId().equals(orderId))
                .findFirst();
    }

    public Long getTrackOffsetByOrderId(Long orderId) {
        return getTracks()
                .stream()
                .filter(t -> t.getOrderId() < orderId)
                .mapToLong(Track::getDuration)
                .sum();
    }

    public Optional<Track> getTrackAfter(Track track) {
        Optional<Track> trackAfter = getTracks()
                .stream()
                .filter(t -> t.getOrderId() > track.getOrderId())
                .findFirst();

        return OptionalUtil.first(trackAfter, getFirstTrack());
    }

    public Optional<Track> getTrackBefore(Track track) {
        Optional<Track> trackBefore = getTracks()
                .stream()
                .filter(t -> t.getOrderId() < track.getOrderId())
                .sorted((o1, o2) -> o2.getOrderId().compareTo(o1.getOrderId()))
                .findFirst();

        return OptionalUtil.first(trackBefore, getLastTrack());
    }

    public Optional<Track> getTrackAtTimePosition(Long time) {
        long initialOffset = 0L;
        return getTracks().stream()
                .filter(statefulPredicate(
                        initialOffset,
                        (offset, track) -> MathUtil.between(offset, offset + track.getDuration(), time),
                        operator((s1, s2) -> s1 + s2, Track::getDuration)
                ))
                .findFirst();
    }

    public Optional<Track> getFirstTrack() {
        return getTracks().stream().findFirst();
    }

    public Optional<Track> getLastTrack() {
        return getTracks().stream()
                .sorted((o1, o2) -> o2.getOrderId().compareTo(o1.getOrderId()))
                .findFirst();
    }

    public Optional<Long> getLongPlayingPositionAt(Long time) {
        if (getChannelState() == ChannelState.STOPPED) {
            return Optional.empty();
        }

        return Optional.of(time - getStartedAt());
    }

    public Optional<Long> getShortPlayingPositionAt(Long time) {
        if (getTracks().size() == 0) {
            return Optional.empty();
        }

        return getLongPlayingPositionAt(time)
                .map(pos -> pos % getTracksDuration());
    }

    public Optional<Track> getPlayingAt(Long time) {
        return getShortPlayingPositionAt(time)
                .flatMap(this::getTrackAtTimePosition);
    }

}
