package com.radioteria.data.dao.impl;

import com.radioteria.data.dao.api.ChannelDao;
import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.Track;
import com.radioteria.data.entities.User;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.Root;
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
