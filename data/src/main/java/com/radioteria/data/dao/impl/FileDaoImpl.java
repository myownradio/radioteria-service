package com.radioteria.data.dao.impl;

import com.radioteria.data.dao.api.FileDao;
import com.radioteria.data.entities.File;
import org.springframework.stereotype.Repository;

@Repository
public class FileDaoImpl extends AbstractDaoImpl<Long, File> implements FileDao {

    public FileDaoImpl() {
        super(Long.class, File.class);
    }

    @Override
    public void increaseLinksCount(File file) {
        file.increaseLinks();
        save(file);
    }

    @Override
    public void decreaseLinksCount(File file) {
        file.decreaseLinks();
        save(file);
    }

}
