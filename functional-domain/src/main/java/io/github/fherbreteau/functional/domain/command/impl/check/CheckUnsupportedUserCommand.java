package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

public class CheckUnsupportedUserCommand extends AbstractCheckUserCommand<UserErrorCommand> {

    private final UserCommandType userCommandType;

    private final UserInput userInput;

    public CheckUnsupportedUserCommand(UserRepository repository, GroupRepository groupRepository,
                                       UserChecker userChecker, UserCommandType type, UserInput userInput) {
        super(repository, groupRepository, userChecker);
        this.userCommandType = type;
        this.userInput = userInput;
    }

    @Override
    protected boolean checkAccess(User actor) {
        return true;
    }

    @Override
    protected UserErrorCommand createSuccess() {
        return new UserErrorCommand(userCommandType, userInput);
    }
}
