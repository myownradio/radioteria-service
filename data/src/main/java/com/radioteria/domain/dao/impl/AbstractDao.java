package com.radioteria.domain.dao.impl;

import com.radioteria.domain.utils.CriteriaCallback;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

@Repository
abstract class AbstractDao<P extends Serializable, E> {

    private Class<P> primaryKeyClass;
    private Class<E> entityClass;

    @Resource
    private SessionFactory sessionFactory;

    public AbstractDao(Class<P> primaryKeyClass, Class<E> entityClass) {
        this.primaryKeyClass = primaryKeyClass;
        this.entityClass = entityClass;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Class<P> getPrimaryKeyClass() {
        return primaryKeyClass;
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void save(E entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    public void delete(E entity) {
        getCurrentSession().delete(entity);
    }

    public E find(P primaryKey) {
        return getCurrentSession().get(getEntityClass(), primaryKey);
    }

    public E findByPropertyValue(String propertyName, String propertyValue) {
        return findByCriteria((criteriaBuilder, criteriaQuery, entityRoot) -> {
            criteriaQuery.select(entityRoot);
            criteriaQuery.where(criteriaBuilder.equal(entityRoot.get(propertyName), propertyValue));

            return getCurrentSession().createQuery(criteriaQuery);
        });
    }

    public E findByCriteria(CriteriaCallback<E> criteriaCallback) {
        return getTypedQueryByCriteria(criteriaCallback).getSingleResult();
    }

    public List<E> listByPropertyValue(String propertyName, String propertyValue) {
        return listByCriteria((criteriaBuilder, criteriaQuery, entityRoot) -> {
            criteriaQuery.select(entityRoot);
            criteriaQuery.where(criteriaBuilder.equal(entityRoot.get(propertyName), propertyValue));

            return getCurrentSession().createQuery(criteriaQuery);
        });
    }

    public List<E> listByCriteria(CriteriaCallback<E> criteriaCallback) {
        return getTypedQueryByCriteria(criteriaCallback).getResultList();
    }

    private TypedQuery<E> getTypedQueryByCriteria(CriteriaCallback<E> criteriaCallback) {
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> entityRoot = criteriaQuery.from(entityClass);

        return criteriaCallback.call(criteriaBuilder, criteriaQuery, entityRoot);
    }

}
