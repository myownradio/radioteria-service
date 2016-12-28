package com.radioteria.data.dao.impl;

import com.radioteria.data.dao.api.FileDao;
import com.radioteria.data.entities.File;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

@Repository
public class FileDaoImpl extends AbstractDaoImpl<Long, File> implements FileDao {

    public FileDaoImpl() {
        super(Long.class, File.class);
    }

    public void increaseLinksCount(File file) {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaUpdate<File> update = cb.createCriteriaUpdate(File.class);
        Root<File> root = update.from(File.class);

        Expression<Long> sum = cb.sum(root.get("linksCount"), 1L);

        update.set(root.<Long>get("linksCount"), sum);
        update.where(cb.equal(root.get("id"), cb.parameter(Long.class, "id")));

        Query query = getEntityManager().createQuery(update);

        query.setParameter("id", file.getId());
        query.executeUpdate();

        getEntityManager().refresh(file);

    }

    public synchronized void decreaseLinksCount(File file) {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaUpdate<File> update = cb.createCriteriaUpdate(File.class);
        Root<File> root = update.from(File.class);

        Expression<Long> diff = cb.diff(root.get("linksCount"), 1L);

        update.set(root.<Long>get("linksCount"), diff);
        update.where(cb.equal(root.get("id"), cb.parameter(Long.class, "id")));

        Query query = getEntityManager().createQuery(update);

        query.setParameter("id", file.getId());
        query.executeUpdate();

        getEntityManager().refresh(file);

    }

}
