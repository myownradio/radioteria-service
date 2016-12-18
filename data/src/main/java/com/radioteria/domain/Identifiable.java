package com.radioteria.domain;

abstract class Identifiable {

    abstract Long getId();

    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identifiable that = (Identifiable) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

}
