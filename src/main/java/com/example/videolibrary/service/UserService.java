package com.example.videolibrary.service;

import com.example.videolibrary.dao.UserDao;
import com.example.videolibrary.dao.impl.UserDaoImpl;
import com.example.videolibrary.model.User;

public class UserService {
    private static UserService instance;
    private final UserDao userDao;

    private UserService() {
        this.userDao = new UserDaoImpl();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public User registerUser(User user) {
        return userDao.save(user);
    }

    public User getUserById(Long id) {
        return userDao.findById(id);
    }

    public User getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
    }

    public User updateUser(User user) {
        return userDao.update(user);
    }
}
