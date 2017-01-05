package com.radioteria.db.dao.api;

import java.util.List;
import java.util.Optional;

public interface AbstractDao<P, E> {

    void persist(E entity);

    E merge(E entity);

    void detach(E entity);

    void delete(E entity);

    void deleteByKey(P key);

    void flush();

    E load(P id);

    Optional<E> find(P id);

    <V> Optional<E> findByPropertyValue(String propertyName, V propertyValue);

    <V> List<E> listByPropertyValue(String propertyName, V propertyValue);

    List<E> list();

    List<E> list(Integer offset, Integer limit);

    Optional<P> findIdByPropertyValue(String propertyName, String propertyValue);

    void clear();

}
