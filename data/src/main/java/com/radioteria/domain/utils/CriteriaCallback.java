package com.radioteria.domain.utils;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public interface CriteriaCallback<E> {
    TypedQuery<E> call(CriteriaBuilder criteriaBuilder, CriteriaQuery<E> criteriaQuery, Root<E> entityRoot);
}
