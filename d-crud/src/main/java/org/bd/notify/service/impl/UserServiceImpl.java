package org.bd.notify.service.impl;

import org.bd.notify.entity.User;
import org.bd.notify.exception.DBException;
import org.bd.notify.repo.UserRepository;
import org.bd.notify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() throws DBException {
        return userRepository.findAll();
    }

    @Override
    public User getUserByName(String name) throws DBException {
        return userRepository.findByName(name);
    }

    @Override
    public User getUserById(Long id) throws DBException {
        return userRepository.findById(id).get();
    }

    @Override
    public User addUser(User user) throws DBException {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User editUser(User user) throws DBException {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteUser(User user) throws DBException {
        userRepository.delete(user);
    }

    @Override
    public Boolean isExistsUser(String name) throws DBException {
        User user;

        try {
            user = getUserByName(name);

            return user != null;
        } catch (NoSuchElementException e) {
            e.getMessage();

            return false;
        }
    }
}