package com.radioteria.data.entities;

import javax.persistence.*;

@Entity
@Table(name = "channels")
@Access(AccessType.FIELD)
public class Channel extends Identifiable<Long> {

    final public static String ID = "id";
    final public static String NAME = "name";
    final public static String DESCRIPTION = "description";
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

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = USER_ID, nullable = false)
    private User user;

    @ManyToOne(targetEntity = File.class)
    @JoinColumn(name = ARTWORK_FILE_ID)
    private File artworkFile;

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

}
