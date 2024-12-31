package io.github.fherbreteau.functional.driving.impl;

import static java.lang.System.Logger.Level.DEBUG;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.CompositeUserCommandFactory;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.domain.user.UserManager;
import io.github.fherbreteau.functional.driving.UserService;

public class UserServiceImpl implements UserService {
    private final System.Logger logger = System.getLogger("FileService");

    private final UserManager userManager;
    private final CompositeUserCommandFactory userCommandFactory;

    public UserServiceImpl(UserManager userManager, CompositeUserCommandFactory userCommandFactory) {
        this.userManager = userManager;
        this.userCommandFactory = userCommandFactory;
    }

    public Output<User> findUserByName(String name) {
        logger.log(DEBUG, "Finding user with name {0}", name);
        return userManager.findUserByName(name);
    }

    public Output<Group> findGroupByName(String name) {
        logger.log(DEBUG, "Finding group with name {0}", name);
        return userManager.findGroupByName(name);
    }

    @Override
    public Output<String> getUserPassword(User user) {
        logger.log(DEBUG, "Loading user password for {0}", user);
        return userManager.getPassword(user);
    }

    @SuppressWarnings("unchecked")
    public <T> Output<T> processCommand(UserCommandType type, User currentUser, UserInput input) {
        logger.log(DEBUG, "Processing command {0} for {1} on {2}", type, currentUser, input);
        CheckCommand<T> command = userCommandFactory.createCommand(type, input);
        return command.execute(currentUser).execute(currentUser);
    }
}
