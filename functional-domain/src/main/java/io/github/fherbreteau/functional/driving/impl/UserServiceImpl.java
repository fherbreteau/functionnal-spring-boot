package io.github.fherbreteau.functional.driving.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.CompositeUserCommandFactory;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.user.UserManager;
import io.github.fherbreteau.functional.driving.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class.getSimpleName());

    private final UserManager userManager;
    private final CompositeUserCommandFactory userCommandFactory;

    public UserServiceImpl(UserManager userManager, CompositeUserCommandFactory userCommandFactory) {
        this.userManager = userManager;
        this.userCommandFactory = userCommandFactory;
    }

    @Override
    public Output<User> findUserByName(String name) {
        logger.debug("Finding user by name");
        return userManager.findUserByName(name);
    }

    @Override
    public Output<Group> findGroupByName(String name) {
        logger.debug("Finding group by name");
        return userManager.findGroupByName(name);
    }

    @Override
    public Output<String> getUserPassword(User user) {
        logger.debug("Loading user password for {}", user);
        return userManager.getPassword(user);
    }

    @Override
    public <T> Output<T> processCommand(UserCommandType type, User currentUser, UserInput input) {
        logger.debug("Processing command {} for {}", type, currentUser);
        CheckCommand<T> command = userCommandFactory.createCommand(type, input);
        return command.execute(currentUser).execute(currentUser);
    }
}
