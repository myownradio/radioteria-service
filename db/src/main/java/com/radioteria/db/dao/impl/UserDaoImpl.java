package com.radioteria.db.dao.impl;

import com.radioteria.db.dao.api.UserDao;
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
