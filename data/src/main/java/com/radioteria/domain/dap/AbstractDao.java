package com.radioteria.domain.dap;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
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
        return  (E) getCurrentSession().get(getEntityClass(), primaryKey);
    }

    public E findByPropertyValue(String propertyName, String propertyValue) {
        return findByCriteria(criteriaBuilder -> {
            CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<E> entityRoot = criteriaQuery.from(entityClass);
            criteriaQuery.select(entityRoot);
            criteriaQuery.where(criteriaBuilder.equal(entityRoot.get(propertyName), propertyValue));
            return criteriaQuery;
        });
    }

    public E findByCriteria(Function<CriteriaBuilder, CriteriaQuery<E>> criteriaCallback) {
        return getTypedQueryByCriteria(criteriaCallback).getSingleResult();
    }

    public List<E> getListByCriteria(Function<CriteriaBuilder, CriteriaQuery<E>> criteriaCallback) {
        return getTypedQueryByCriteria(criteriaCallback).getResultList();
    }

    private TypedQuery<E> getTypedQueryByCriteria(Function<CriteriaBuilder, CriteriaQuery<E>> criteriaCallback) {
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<E> query = criteriaCallback.apply(criteriaBuilder);

        Root<E> from = query.from(entityClass);
        query.select(from);

        return getCurrentSession().createQuery(query);
    }

}
