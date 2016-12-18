package com.radioteria.domain.dao.impl;

import com.radioteria.domain.utils.CriteriaCallback;
import com.radioteria.domain.entities.Identifiable;

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

@Repository
abstract class AbstractDao<P extends Serializable, E extends Identifiable<P>> {

    private Class<P> idClass;
    private Class<E> entityClass;

    @Resource
    private SessionFactory sessionFactory;

    public AbstractDao(Class<P> idClass, Class<E> entityClass) {
        this.idClass = idClass;
        this.entityClass = entityClass;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Class<P> getIdClass() {
        return idClass;
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

    public E find(P id) {
        return getCurrentSession().get(getEntityClass(), id);
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

    public P findIdByPropertyValue(String propertyName, String propertyValue) {
        E entity = findByPropertyValue(propertyName, propertyValue);
        if (entity == null) {
            return null;
        }
        return entity.getId();
    }

    // todo: http://www.objectdb.com/java/jpa/query/jpql/structure
    private TypedQuery<E> getTypedQueryByCriteria(CriteriaCallback<E> criteriaCallback) {
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> entityRoot = criteriaQuery.from(entityClass);

        return criteriaCallback.call(criteriaBuilder, criteriaQuery, entityRoot);
    }

}
