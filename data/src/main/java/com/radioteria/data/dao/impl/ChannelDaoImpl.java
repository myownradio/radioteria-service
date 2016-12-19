package com.radioteria.data.dao.impl;

import com.radioteria.data.dao.api.ChannelDao;
import com.radioteria.data.entities.Channel;
import org.springframework.stereotype.Repository;

@Repository
public class ChannelDaoImpl extends AbstractDaoImpl<Long, Channel> implements ChannelDao {

    public ChannelDaoImpl() {
        super(Long.class, Channel.class);
    }

}
