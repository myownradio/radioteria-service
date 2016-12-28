package com.radioteria.data.dao.impl;

import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl extends AbstractDaoImpl<Long, User> implements UserDao {

    public UserDaoImpl() {
        super(Long.class, User.class);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.findByPropertyValue(User.EMAIL, email);
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return !findByEmail(email).isPresent();
    }

}
