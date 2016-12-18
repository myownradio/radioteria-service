package com.radioteria.domain.dao.api;

import com.radioteria.domain.User;

public interface UserDao {
    User findByEmail(String email);
}
