package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.List;

public class CheckUnsupportedUserCommand extends AbstractCheckUserCommand<UserErrorCommand> {

    private final UserCommandType userCommandType;

    private final UserInput userInput;

    public CheckUnsupportedUserCommand(UserRepository repository, GroupRepository groupRepository,
                                       UserChecker userChecker, PasswordProtector passwordProtector,
                                       UserCommandType type, UserInput userInput) {
        super(repository, groupRepository, userChecker, passwordProtector);
        this.userCommandType = type;
        this.userInput = userInput;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        return List.of();
    }

    @Override
    protected UserErrorCommand createSuccess() {
        return new UserErrorCommand(userCommandType, userInput);
    }
}
