package com.radioteria.data.dao.api;

import com.radioteria.data.entities.User;

public interface UserDao extends AbstractDao<Long, User> {
    User findByEmail(String email);
    boolean isEmailAvailable(String email);
}
