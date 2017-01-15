package com.radioteria.db.dao

import com.radioteria.db.entities.Entity
import java.io.Serializable
import javax.persistence.criteria.CriteriaQuery

interface EntityRepository<K : Serializable, E : Entity<K>> {
    /**
     * Make an instance managed and persistent.
     */
    fun persist(entity: E)

    /**
     * Remove the entity instance.
     */
    fun remove(entity: E)

    /**
     * Find by primary key.
     */
    fun find(key: K): E?

    /**
     * Find by queryBuilder.
     */
    fun find(cqProvider: () -> CriteriaQuery<E>): E?

    /**
     * List all entities.
     */
    fun list(): List<E>

    /**
     * List entities by queryBuilder.
     */
    fun list(cqProvider: () -> CriteriaQuery<E>): List<E>
}
