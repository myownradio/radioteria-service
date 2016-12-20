package com.radioteria.data.entities;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class File extends Identifiable<Long> {

    final public static String ID = "id";
    final public static String LINKS_COUNT = "links_count";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = ID, nullable = false, updatable = false)
    private Long id;

    @Column(name = LINKS_COUNT, nullable = false)
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
