package org.bd.notify.repo;

import org.bd.notify.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findAllById(long id);
}