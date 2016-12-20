package com.radioteria.data.entities;


import javax.persistence.*;

@Entity
@Table(name = "tracks")
public class Track extends Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "artist", nullable = false)
    private String artist;

    @Column(name = "duration", nullable = false)
    private Long duration;

    @ManyToOne(targetEntity = File.class)
    @JoinColumn(name = "track_file_id", nullable = false)
    private File trackFile;

    @ManyToOne(targetEntity = Channel.class)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    public Long getId() {
        return id;
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

}
