package com.radioteria.data.dao.api;

import com.radioteria.data.utils.CriteriaCallback;

import java.util.List;

public interface AbstractDao<P, E> {

    void persist(E entity);

    E merge(E entity);

    void detach(E entity);

    void delete(E entity);

    void deleteByKey(P key);

    void flush();

    E load(P id);

    E find(P id);

    <V> E findByPropertyValue(String propertyName, V propertyValue);

    E findByCriteria(CriteriaCallback<E> criteriaCallback);

    <V> List<E> listByPropertyValue(String propertyName, V propertyValue);

    List<E> list();

    List<E> listByCriteria(CriteriaCallback<E> criteriaCallback);

    P findIdByPropertyValue(String propertyName, String propertyValue);

    void clear();

}
