package com.radioteria.db.dao

import com.radioteria.db.entities.IdAwareEntity
import java.io.Serializable
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaQuery

abstract class JpaEntityRepository<K : Serializable, E : IdAwareEntity<K>>(
        val entityClass: Class<E>,
        val idClass: Class<K>,
        val entityManager: EntityManager
) : EntityRepository<K, E> {

    override fun persist(entity: E) {
        entityManager.persist(entity)
    }

    override fun remove(entity: E) {
        entityManager.remove(entity)
    }

    override fun find(id: K): E? {
        return entityManager.find(entityClass, id)
    }

    override fun find(cqProvider: () -> CriteriaQuery<E>): E? {
        return try { query(cqProvider).singleResult } catch (e: NoResultException) { null }
    }

    override fun list(): List<E> {
        return list { entityManager.criteriaBuilder.createQuery(entityClass) }
    }

    override fun list(cqProvider: () -> CriteriaQuery<E>): List<E> {
        return query(cqProvider).resultList
    }

    override fun <T> query(cqProvider: () -> CriteriaQuery<T>): TypedQuery<T> {
        return entityManager.createQuery(cqProvider.invoke())
    }
}