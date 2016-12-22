package com.radioteria.data.dao.impl;

import com.radioteria.data.dao.api.AbstractDao;
import com.radioteria.data.entities.Identifiable;
import com.radioteria.data.utils.CriteriaCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    public void persist(E entity) {
        getEntityManager().persist(entity);
    }

    @SuppressWarnings("unchecked")
    public E merge(E entity) {
        return (E) getEntityManager().merge(entity);
    }

    public void detach(E entity) {
        getEntityManager().detach(entity);
    }

    public void delete(E entity) {
        getEntityManager().remove(entity);
    }

    public void deleteByKey(P key) {
        E entity = load(key);
        delete(entity);
    }

    public void flush() {
        getEntityManager().flush();
    }

    public E load(P id) {
        return getEntityManager().getReference(getEntityClass(), id);
    }

    public E find(P id) {
        return getEntityManager().find(getEntityClass(), id);
    }

    public E findByPropertyValue(String propertyName, String propertyValue) {
        return findByCriteria((criteriaBuilder, criteriaQuery, entityRoot) -> {
            criteriaQuery.select(entityRoot);
            criteriaQuery.where(criteriaBuilder.equal(entityRoot.get(propertyName), propertyValue));

            return getEntityManager().createQuery(criteriaQuery);
        });
    }

    public E findByCriteria(CriteriaCallback<E> criteriaCallback) {
        return getTypedQueryByCriteria(criteriaCallback).getSingleResult();
    }

    public List<E> listByPropertyValue(String propertyName, String propertyValue) {
        return listByCriteria((criteriaBuilder, criteriaQuery, entityRoot) -> {
            criteriaQuery.select(entityRoot);
            criteriaQuery.where(criteriaBuilder.equal(entityRoot.get(propertyName), propertyValue));

            return getEntityManager().createQuery(criteriaQuery);
        });
    }

    public List<E> list() {
        return listByCriteria((cb, cq, er) -> getEntityManager().createQuery(cq));
    }

    public List<E> list(int offset, Integer limit) {
        return listByCriteria((criteriaBuilder, criteriaQuery, entityRoot) -> {
            TypedQuery<E> query = getEntityManager().createQuery(criteriaQuery);
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
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> entityRoot = criteriaQuery.from(entityClass);

        return criteriaCallback.call(criteriaBuilder, criteriaQuery, entityRoot);
    }

    public void clear() {
        getEntityManager().clear();
    }

}
