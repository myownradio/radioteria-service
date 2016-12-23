package com.radioteria.data.dao.impl;

import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends AbstractDaoImpl<Long, User> implements UserDao {

    public UserDaoImpl() {
        super(Long.class, User.class);
    }

    @Override
    public User findByEmail(String email) {
        return this.findByPropertyValue(User.EMAIL, email);
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return findByEmail(email) != null;
    }

}
