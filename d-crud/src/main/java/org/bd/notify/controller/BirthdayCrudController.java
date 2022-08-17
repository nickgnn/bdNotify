package org.bd.notify.controller;

import org.bd.notify.entity.User;
import org.bd.notify.exception.DBException;
import org.bd.notify.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/users")
public class BirthdayCrudController {
    private final Logger logger = Logger.getLogger(BirthdayCrudController.class.getName());

    @Autowired
    private final UserService userService;

    @Autowired
    public BirthdayCrudController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/all")
    public List<User> getAllUsers() throws DBException {
        List<User> users = userService.getAllUsers();

        logger.info(users.toString());

        return users;
    }

    @GetMapping("{id}")
    public User getOneUser(@PathVariable("id") User user) {
        return user;
    }

    @PostMapping(value = "/createUser")
    public User createUser(@RequestBody User user) throws DBException {
        logger.info("User " + user.getName() + " was created :)");

        return userService.addUser(user);
    }

    @PutMapping("{id}")
    public User updateUser(@PathVariable("id") User userFromDB, @RequestBody User user) throws DBException {
        BeanUtils.copyProperties(user, userFromDB, "id");

        return userService.editUser(userFromDB);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") User user) throws DBException {
        logger.info("User " + user.getName() + " was deleted :(");

        userService.deleteUser(user);
    }
}