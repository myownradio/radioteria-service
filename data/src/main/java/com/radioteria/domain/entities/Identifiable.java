package com.radioteria.domain.entities;

abstract public class Identifiable<K> {

    abstract public K getId();

    @Override
    public int hashCode() {
        if (getId() == null) {
            return 0;
        }
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identifiable that = (Identifiable) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

}
