package com.radioteria.db.dao.impl;

import com.radioteria.db.dao.api.ContentDao;
import org.springframework.stereotype.Repository;

@Repository
public class ContentDaoImpl extends AbstractDaoImpl<Long, Content> implements ContentDao {
    public ContentDaoImpl() {
        super(Long.class, Content.class);
    }
}
