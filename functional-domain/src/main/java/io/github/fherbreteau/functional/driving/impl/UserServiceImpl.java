package io.github.fherbreteau.functional.driving.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.CompositeUserCommandFactory;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.user.UserManager;
import io.github.fherbreteau.functional.driving.UserService;

public class UserServiceImpl implements UserService {

    private final UserManager userManager;
    private final CompositeUserCommandFactory userCommandFactory;

    public UserServiceImpl(UserManager userManager, CompositeUserCommandFactory userCommandFactory) {
        this.userManager = userManager;
        this.userCommandFactory = userCommandFactory;
    }

    public Output<User> findUserByName(String name) {
        return userManager.findUserByName(name);
    }

    public Output<Group> findGroupByName(String name) {
        return userManager.findGroupByName(name);
    }

    @SuppressWarnings("unchecked")
    public <T> Output<T> processCommand(UserCommandType type, User currentUser, UserInput userInput) {
        CheckCommand<T> command = userCommandFactory.createCommand(type, userInput);
        return command.execute(currentUser).execute(currentUser);
    }
}
