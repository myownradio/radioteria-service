package com.radioteria.db.repositories

import com.radioteria.db.entities.IdAwareEntity
import java.io.Serializable
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaQuery

interface EntityRepository<K : Serializable, E : IdAwareEntity<K>> {
    /**
     * Make an instance managed and persistent.
     */
    fun persist(entity: E)

    /**
     * Remove the entity instance.
     */
    fun remove(entity: E)

    /**
     * Find by primary id.
     */
    fun find(id: K): E?

    /**
     * Find by criteria query.
     */
    fun find(cqProvider: () -> CriteriaQuery<E>): E?

    /**
     * Find by property value.
     */
    fun <T> find(propertyName: String, propertyValue: T): E?

    /**
     * List all entities.
     */
    fun list(): List<E>

    /**
     * List entities by criteria query.
     */
    fun list(cqProvider: () -> CriteriaQuery<E>): List<E>

    /**
     * List entities by property value.
     */
    fun <T> list(propertyName: String, propertyValue: T): List<E>

    /**
     * Create typed query by criteria query.
     */
    fun query(cqProvider: () -> CriteriaQuery<E>): TypedQuery<E>

    /**
     * Create typed query by property value.
     */
    fun <T> query(propertyName: String, propertyValue: T): TypedQuery<E>

    /**
     * Synchronize the persistence context to the underlying database.
     */
    fun flush()
}
