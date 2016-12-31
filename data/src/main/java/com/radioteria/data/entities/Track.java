package com.radioteria.data.entities;


import javax.persistence.*;

@Entity
@Table(name = "tracks")
public class Track extends Identifiable<Long> {

    final public static String ID = "id";
    final public static String ORDER_ID = "order_id";
    final public static String TITLE = "title";
    final public static String ARTIST = "artist";
    final public static String DURATION = "duration";
    final public static String TRACK_FILE_ID = "track_file_id";
    final public static String CHANNEL_ID = "channel_id";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = ID, nullable = false, updatable = false)
    private Long id;

    @Column(name = ORDER_ID, nullable = false)
    private Long orderId;

    @Column(name = TITLE, nullable = false)
    private String title;

    @Column(name = ARTIST, nullable = false)
    private String artist;

    @Column(name = DURATION, nullable = false)
    private Long duration;

    @ManyToOne(targetEntity = File.class)
    @JoinColumn(name = TRACK_FILE_ID)
    private File trackFile;

    @ManyToOne(targetEntity = Channel.class)
    @JoinColumn(name = CHANNEL_ID, nullable = false)
    private Channel channel;

    public Track() {}

    public Track(String title, Long duration) {
        this.setTitle(title);
        this.setDuration(duration);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public File getTrackFile() {
        return trackFile;
    }

    public void setTrackFile(File trackFile) {
        this.trackFile = trackFile;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                '}';
    }
}
