package com.radioteria.db.dao.impl;

import com.radioteria.db.dao.api.AbstractDao;
import com.radioteria.db.entities.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

abstract class AbstractDaoImpl<P extends Serializable, E extends BaseEntity<P>> implements AbstractDao<P, E> {

    private Class<P> idClass;
    private Class<E> entityClass;

    @PersistenceContext
    private EntityManager entityManager;

    public AbstractDaoImpl(Class<P> idClass, Class<E> entityClass) {
        this.idClass = idClass;
        this.entityClass = entityClass;
    }

    protected Class<P> getIdClass() {
        return idClass;
    }

    protected Class<E> getEntityClass() {
        return entityClass;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void persist(E entity) {
        getEntityManager().persist(entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E merge(E entity) {
        return (E) getEntityManager().merge(entity);
    }

    @Override
    public void detach(E entity) {
        getEntityManager().detach(entity);
    }

    @Override
    public void delete(E entity) {
        getEntityManager().remove(entity);
    }

    @Override
    public void deleteByKey(P key) {
        E entity = load(key);
        delete(entity);
    }

    @Override
    public void flush() {
        getEntityManager().flush();
    }

    @Override
    public E load(P id) {
        return getEntityManager().getReference(getEntityClass(), id);
    }

    @Override
    public Optional<E> find(P id) {
        return Optional.ofNullable(getEntityManager().find(getEntityClass(), id));
    }

    @Override
    public <V> Optional<E> findByPropertyValue(String propertyName, V propertyValue) {
        TypedQuery<E> typedQuery = getQueryByPropertyValue(propertyName, propertyValue);

        return Optional.ofNullable(typedQuery.getSingleResult());
    }

    @Override
    public <V> List<E> listByPropertyValue(String propertyName, V propertyValue) {
        return getQueryByPropertyValue(propertyName, propertyValue).getResultList();
    }

    private <V> TypedQuery<E> getQueryByPropertyValue(String propertyName, V propertyValue) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        Root<E> root = query.from(getEntityClass());

        query.select(root);

        query.where(cb.equal(root.get(propertyName), propertyValue));

        return getEntityManager().createQuery(query);
    }

    @Override
    public List<E> list() {

        TypedQuery<E> typedQuery = getListQuery();

        return typedQuery.getResultList();

    }

    @Override
    public List<E> list(Integer offset, Integer limit) {

        TypedQuery<E> typedQuery = getListQuery();

        typedQuery.setFirstResult(offset);

        if (limit != null) {
            typedQuery.setMaxResults(limit);
        }

        return typedQuery.getResultList();

    }

    private TypedQuery<E> getListQuery() {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());

        query.select(query.from(getEntityClass()));

        return getEntityManager().createQuery(query);

    }

    @Override
    public Optional<P> findIdByPropertyValue(String propertyName, String propertyValue) {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<P> query = cb.createQuery(getIdClass());

        Root<E> root = query.from(getEntityClass());

        query.select(root.get("id"));

        query.where(cb.equal(root.get(propertyName), propertyValue));

        return Optional.ofNullable(getEntityManager().createQuery(query).getSingleResult());

    }

    @Override
    public void clear() {
        getEntityManager().clear();
    }

}
