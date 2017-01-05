package com.radioteria.db.dao.api;

import com.radioteria.db.entities.User;

import java.util.Optional;

public interface UserDao extends AbstractDao<Long, User> {
    Optional<User> findByEmail(String email);
    boolean isEmailAvailable(String email);
}
