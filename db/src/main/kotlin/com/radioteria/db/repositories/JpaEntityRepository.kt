package com.radioteria.db.repositories

import com.radioteria.db.entities.IdAwareEntity
import org.omg.CORBA.Object
import java.io.Serializable
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import javax.persistence.PersistenceContext
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaQuery

abstract class JpaEntityRepository<K : Serializable, E : IdAwareEntity<K>>(
        val entityClass: Class<E>,
        val idClass: Class<K>) : EntityRepository<K, E> {

    @PersistenceContext lateinit var entityManager: EntityManager

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

    override fun <T> find(propertyName: String, propertyValue: T): E? {
       return try { query(propertyName, propertyValue).singleResult } catch (e: NoResultException) { null }
    }

    override fun list(): List<E> {
        return list {
            with(entityManager.criteriaBuilder.createQuery(entityClass)) {
                this.select(this.from(entityClass))
            }
        }
    }

    override fun <T> list(propertyName: String, propertyValue: T): List<E> {
        return query(propertyName, propertyValue).resultList
    }

    override fun list(cqProvider: () -> CriteriaQuery<E>): List<E> {
        return query(cqProvider).resultList
    }

    override fun query(cqProvider: () -> CriteriaQuery<E>): TypedQuery<E> {
        return entityManager.createQuery(cqProvider.invoke())
    }

    override fun <T> query(propertyName: String, propertyValue: T): TypedQuery<E> {
        val cb = entityManager.criteriaBuilder
        val criteriaQuery = cb.createQuery(entityClass)
        val root = criteriaQuery.from(entityClass)

        return entityManager.createQuery(criteriaQuery
                .select(root).where(cb.equal(root.get<T>(propertyName), propertyValue)))
    }

    override fun flush() {
        entityManager.flush()
    }
}