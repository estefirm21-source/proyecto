package com.greencert.db.dao;

import com.greencert.db.model.User;

public interface UserDAO {
    void save(User user);
    User findByUsername(String username);
}
