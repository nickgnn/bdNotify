package org.bd.notify.service;

import org.bd.notify.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService {
    String getAllUsers();
    User getOneUser(Long id);
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    String getBirthday(String url);
}