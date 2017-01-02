package com.radioteria.data.dao.impl;

import com.radioteria.data.dao.api.ContentDao;
import com.radioteria.data.entities.Content;
import org.springframework.stereotype.Repository;

@Repository
public class ContentDaoImpl extends AbstractDaoImpl<Long, Content> implements ContentDao {
    public ContentDaoImpl() {
        super(Long.class, Content.class);
    }
}
