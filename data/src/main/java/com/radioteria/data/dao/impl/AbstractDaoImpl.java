package com.radioteria.data.dao.impl;

import com.radioteria.data.dao.api.AbstractDao;
import com.radioteria.data.utils.CriteriaCallback;
import com.radioteria.data.entities.Identifiable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

@Repository
abstract class AbstractDaoImpl<P extends Serializable, E extends Identifiable<P>> implements AbstractDao<P, E> {

    private Class<P> idClass;
    private Class<E> entityClass;

    @Resource
    private SessionFactory sessionFactory;

    public AbstractDaoImpl(Class<P> idClass, Class<E> entityClass) {
        this.idClass = idClass;
        this.entityClass = entityClass;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    protected Class<P> getIdClass() {
        return idClass;
    }

    protected Class<E> getEntityClass() {
        return entityClass;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void persist(E entity) {
        getCurrentSession().persist(entity);
    }

    @SuppressWarnings("unchecked")
    public E merge(E entity) {
        return (E) getCurrentSession().merge(entity);
    }

    public void delete(E entity) {
        getCurrentSession().delete(entity);
    }

    public void deleteByKey(P key) {
        E entity = load(key);
        delete(entity);
    }

    public void flush() {
        getCurrentSession().flush();
    }

    public E load(P id) {
        return getCurrentSession().load(getEntityClass(), id);
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

    public List<E> list() {
        return listByCriteria((criteriaBuilder, criteriaQuery, entityRoot) ->
            getCurrentSession().createQuery(criteriaQuery)
        );
    }

    public List<E> list(int offset, Integer limit) {
        return listByCriteria((criteriaBuilder, criteriaQuery, entityRoot) -> {
            Query<E> query = getCurrentSession().createQuery(criteriaQuery);
            query.setFirstResult(offset);
            if (limit != null) {
                query.setMaxResults(limit);
            }
            return query;
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
