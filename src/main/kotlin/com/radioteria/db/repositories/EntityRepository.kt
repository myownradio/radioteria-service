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
    fun findById(id: K): E?

    /**
     * Find by property value.
     */
    fun <T> findByPropertyValue(propertyName: String, propertyValue: T): E?

    /**
     * List all entities.
     */
    fun list(): List<E>

    /**
     * List entities by property value.
     */
    fun <T> listByPropertyValue(propertyName: String, propertyValue: T): List<E>

    /**
     * Synchronize the persistence context to the underlying database.
     */
    fun flush()

    /**
     * Increment property value by amount.
     */
    fun <A : Number> increment(entity: E, propertyName: String, amount: A)
}
