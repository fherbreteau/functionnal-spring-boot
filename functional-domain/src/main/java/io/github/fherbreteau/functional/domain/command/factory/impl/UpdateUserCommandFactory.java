package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.UserCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckUpdateUserCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.*;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

import static java.util.Objects.nonNull;

public class UpdateUserCommandFactory implements UserCommandFactory<User> {
    @Override
    public boolean supports(UserCommandType type, UserInput userInput) {
        return type == UserCommandType.USERMOD && isValidModify(userInput) ||
                type == UserCommandType.PASSWD && isValidPasswd(userInput);
    }

    private boolean isValidModify(UserInput userInput) {
        return nonNull(userInput.getName()) && (nonNull(userInput.getUserId()) || nonNull(userInput.getNewName()) ||
                nonNull(userInput.getPassword()) || nonNull(userInput.getGroupId()) ||
                !userInput.getGroups().isEmpty());
    }

    private boolean isValidPasswd(UserInput userInput) {
        return nonNull(userInput.getName()) && nonNull(userInput.getPassword());
    }

    @Override
    public CheckCommand<User> createCommand(UserRepository repository, GroupRepository groupRepository,
                                            UserChecker userChecker, UserUpdater userUpdater,
                                            PasswordProtector passwordProtector, UserCommandType type,
                                            UserInput userInput) {
        return new CheckUpdateUserCommand(repository, groupRepository, userChecker, userUpdater, passwordProtector,
                type, userInput);
    }
}
