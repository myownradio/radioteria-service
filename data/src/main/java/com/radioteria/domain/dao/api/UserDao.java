package com.radioteria.domain.dao.api;

import com.radioteria.domain.entities.User;

public interface UserDao {
    User findByEmail(String email);
}
