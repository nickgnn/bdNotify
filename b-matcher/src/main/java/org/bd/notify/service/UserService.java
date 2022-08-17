package org.bd.notify.service;

import org.bd.notify.entity.User;
import org.bd.notify.exception.DBException;

public interface UserService {
    Iterable<User> getAllUsers() throws DBException;
}