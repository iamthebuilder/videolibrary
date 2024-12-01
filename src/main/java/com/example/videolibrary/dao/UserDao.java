package com.example.videolibrary.dao;

import com.example.videolibrary.model.User;

public interface  UserDao {
    User save(User user);
    User findById(Long id);
    User findByEmail(String email);
    void delete(Long id);
    User update(User user);
}
