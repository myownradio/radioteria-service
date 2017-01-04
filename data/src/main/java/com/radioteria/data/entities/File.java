package com.radioteria.data.entities;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class File extends BaseEntity<Long> {
    final public static String ID = "id";
    final public static String NAME = "name";
    final public static String CONTENT_ID = "content_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID, nullable = false, updatable = false)
    private Long id;

    @Column(name = NAME, nullable = false)
    private String name;

    @ManyToOne(targetEntity = Content.class)
    @JoinColumn(name = CONTENT_ID, nullable = false)
    private Content content;

    @Override
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

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
