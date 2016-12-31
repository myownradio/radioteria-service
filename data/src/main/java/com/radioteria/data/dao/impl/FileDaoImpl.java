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



}
