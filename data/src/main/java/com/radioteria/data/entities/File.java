package com.radioteria.data.entities;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class File extends Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

}
