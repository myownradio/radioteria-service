package com.radioteria.data.entities;

abstract public class Identifiable<K> {

    abstract public K getId();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        return getId() != null ? getId().equals(track.getId()) : track.getId() == null;

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

}
