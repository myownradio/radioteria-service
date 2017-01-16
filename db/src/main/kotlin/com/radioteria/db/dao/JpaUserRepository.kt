package com.radioteria.db.dao

import com.radioteria.db.entities.User
import com.radioteria.db.entities.UserMeta
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class JpaUserRepository(entityManager: EntityManager) :
        JpaEntityRepository<Long, User>(User::class.java, Long::class.java, entityManager), UserRepository {

    override fun findByEmail(email: String): User? {
        return find {
            val cb = entityManager.criteriaBuilder
            val criteriaQuery = cb.createQuery(entityClass)
            val root = criteriaQuery.from(entityClass)

            criteriaQuery
                    .select(root)
                    .where(cb.equal(root.get<User>(UserMeta.EMAIL), email))
        }
    }

    override fun isEmailAvailable(email: String): Boolean = findByEmail(email) == null

}