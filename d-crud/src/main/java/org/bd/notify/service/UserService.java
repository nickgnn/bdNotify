package org.bd.notify.service;

import org.bd.notify.entity.User;
import org.bd.notify.exception.DBException;

import java.util.List;

public interface UserService {
    List<User> getAllUsers() throws DBException;
    User getUserByName(String name) throws DBException;
    User getUserById(Long id) throws DBException;
    User addUser(User user) throws DBException;
    User editUser(User user) throws DBException;
    void deleteUser(User user) throws DBException;
    Boolean isExistsUser(String name) throws DBException;
}
