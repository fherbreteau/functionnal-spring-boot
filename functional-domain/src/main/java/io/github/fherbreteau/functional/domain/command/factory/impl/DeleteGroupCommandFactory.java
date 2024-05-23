package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.UserCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckDeleteGroupCommand;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.*;

import static java.util.Objects.nonNull;

public class DeleteGroupCommandFactory implements UserCommandFactory {
    @Override
    public boolean supports(UserCommandType type, UserInput userInput) {
        return type == UserCommandType.GROUPDEL && isValid(userInput);
    }

    private boolean isValid(UserInput userInput) {
        return nonNull(userInput.getName());
    }

    @Override
    public CheckCommand<Output> createCommand(UserRepository repository, GroupRepository groupRepository,
                                              UserChecker userChecker, UserUpdater userUpdater,
                                              PasswordProtector passwordProtector, UserCommandType type,
                                              UserInput userInput) {
        return new CheckDeleteGroupCommand(repository, groupRepository, userChecker, userUpdater,
                userInput.getName(), userInput.isForce());
    }
}
