package com.radioteria.db.dao

import javax.persistence.criteria.CriteriaQuery

interface QueryBuilder<E> {
    fun createCriteriaQuery(): CriteriaQuery<E>
}