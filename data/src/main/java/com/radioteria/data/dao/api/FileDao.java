package com.radioteria.data.dao.api;

import com.radioteria.data.entities.File;

public interface FileDao extends AbstractDao<Long, File> {
    void increaseLinksCount(File file);
    void decreaseLinksCount(File file);
}
