package com.radioteria.db.dao.impl

import com.radioteria.db.dao.EntityRepository
import com.radioteria.db.entities.IdAware
import java.io.Serializable
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

@PersistenceContext
abstract class BaseDaoImpl<K : Serializable, E : IdAware<K>>(
        val keyClass: Class<K>, val entityClass: Class<E>,
        val entityManager: EntityManager) : EntityRepository<K, E> {

    override fun persist(entity: E) = entityManager.persist(entity)
    override fun remove(entity: E) = entityManager.remove(entity)
    override fun find(key: K): E? = entityManager.find(entityClass, key)

    override fun list(): List<E> = entityManager.createQuery(entityManager.criteriaBuilder.createQuery(entityClass)).resultList

    override fun list(offset: Int, limit: Int): Array<E> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByPropertyValue(key: String, value: String): E? {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listByPropertyValue(key: String, value: String): Array<E> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun queryByPropertyValue(key: String, value: String): TypedQuery<E> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(entityClass)
        val root = query.from(entityClass)

        query.select(root)

        query.where(cb.equal(root.get<E>(key), value))

        return entityManager.createQuery(query)
    }

    override fun flush() = entityManager.flush()
    override fun clear() = entityManager.clear()
}