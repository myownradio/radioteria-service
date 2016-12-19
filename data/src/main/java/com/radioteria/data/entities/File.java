package com.radioteria.data.entities;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class File extends Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "links_count", nullable = false)
    private Long linksCount = 0L;

    @Override
    public Long getId() {
        return id;
    }

    public Long getLinksCount() {
        return linksCount;
    }

    public void increaseLinks() {
        linksCount ++;
    }

    public void decreaseLinks() {
        linksCount --;
    }

}
