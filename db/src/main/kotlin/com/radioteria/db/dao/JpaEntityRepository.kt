package com.radioteria.db.dao

import com.radioteria.db.entities.Entity
import java.io.Serializable
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaQuery


class JpaEntityRepository<K : Serializable, E : Entity<K>>(
        val entityClass: Class<E>, val idClass: Class<K>,
        val entityManager: EntityManager) : EntityRepository<K, E> {

    override fun persist(entity: E) {
        entityManager.persist(entity)
    }

    override fun remove(entity: E) {
        entityManager.remove(entity)
    }

    override fun find(key: K): E? {
        return entityManager.find(entityClass, key)
    }

    override fun find(cqProvider: () -> CriteriaQuery<E>): E? {
        return createQuery(cqProvider).singleResult
    }

    override fun list(): List<E> {
        return list { entityManager.criteriaBuilder.createQuery(entityClass) }
    }

    override fun list(cqProvider: () -> CriteriaQuery<E>): List<E> {
        return createQuery(cqProvider).resultList
    }

    private fun createQuery(cqProvider: () -> CriteriaQuery<E>): TypedQuery<E> {
        return entityManager.createQuery(cqProvider.invoke())
    }
}