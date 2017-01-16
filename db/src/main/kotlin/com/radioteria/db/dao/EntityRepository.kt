package com.radioteria.db.dao

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
     * List all entities.
     */
    fun list(): List<E>

    /**
     * List entities by criteria query.
     */
    fun list(cqProvider: () -> CriteriaQuery<E>): List<E>

    /**
     * Create typed query by criteria query.
     */
    fun <T> query(cqProvider: () -> CriteriaQuery<T>): TypedQuery<T>
}
