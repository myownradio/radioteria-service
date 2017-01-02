package com.radioteria.data.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "content")
@Access(AccessType.FIELD)
public class Content {
    final public static String ID = "id";
    final public static String HASH = "hash";
    final public static String TYPE = "type";
    final public static String LENGTH = "length";

    @Id
    @GeneratedValue
    @Column(name = ID, nullable = false, updatable = false)
    private Long id;

    @Column(name = HASH, nullable = false, unique = true)
    private String hash;

    @Column(name = TYPE, nullable = false)
    private String type;

    @Column(name = LENGTH, nullable = false)
    private Long length;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }
}
