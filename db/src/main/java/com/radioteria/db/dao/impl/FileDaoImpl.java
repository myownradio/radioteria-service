package com.radioteria.db.dao.impl;

import com.radioteria.db.dao.api.FileDao;
import com.radioteria.db.entities.File;
import org.springframework.stereotype.Repository;

@Repository
public class FileDaoImpl extends AbstractDaoImpl<Long, File> implements FileDao {

    public FileDaoImpl() {
        super(Long.class, File.class);
    }



}
