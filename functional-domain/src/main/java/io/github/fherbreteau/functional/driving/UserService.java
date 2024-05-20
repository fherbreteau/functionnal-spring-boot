package io.github.fherbreteau.functional.driving;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.CompositeUserCommandFactory;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.user.UserManager;

public class UserService {

    private final UserManager userManager;
    private final CompositeUserCommandFactory userCommandFactory;

    public UserService(UserManager userManager, CompositeUserCommandFactory userCommandFactory) {
        this.userManager = userManager;
        this.userCommandFactory = userCommandFactory;
    }

    public Output findUserByName(String name) {
        return userManager.findUserByName(name);
    }

    public Output findGroupByName(String name) {
        return userManager.findGroupByName(name);
    }

    public Output processCommand(UserCommandType type, User currentUser, UserInput userInput) {
        CheckCommand<Output> command = userCommandFactory.createCommand(type, userInput);
        return command.execute(currentUser).execute(currentUser);
    }
}
