package com.radioteria.db.dao.impl;

import com.radioteria.db.dao.api.ChannelDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChannelDaoImpl extends AbstractDaoImpl<Long, Channel> implements ChannelDao {

    public ChannelDaoImpl() {
        super(Long.class, Channel.class);
    }

    public List<Channel> findByUser(User user) {
        return findByUserId(user.getId());
    }

    public List<Channel> findByUserId(Long userId) {
        return listByPropertyValue(Channel.USER_ID, userId);
    }

}
