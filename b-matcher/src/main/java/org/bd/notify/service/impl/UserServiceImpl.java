package org.bd.notify.service.impl;

import org.bd.notify.entity.User;
import org.bd.notify.repo.UserRepository;
import org.bd.notify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}