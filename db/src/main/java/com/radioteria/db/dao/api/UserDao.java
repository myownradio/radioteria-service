package com.radioteria.db.dao.api;

import java.util.Optional;

public interface UserDao extends AbstractDao<Long, User> {
    Optional<User> findByEmail(String email);
    boolean isEmailAvailable(String email);
}
