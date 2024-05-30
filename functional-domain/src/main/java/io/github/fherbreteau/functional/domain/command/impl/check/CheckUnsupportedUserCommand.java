package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.*;

import java.util.List;

public class CheckUnsupportedUserCommand extends AbstractCheckUserCommand<Void, UserErrorCommand<Void>> {

    private final UserCommandType userCommandType;

    private final UserInput userInput;

    public CheckUnsupportedUserCommand(UserRepository repository, GroupRepository groupRepository,
                                       UserChecker userChecker, UserUpdater userUpdater,
                                       UserCommandType type, UserInput userInput) {
        super(repository, groupRepository, userChecker, userUpdater);
        this.userCommandType = type;
        this.userInput = userInput;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        return List.of();
    }

    @Override
    protected UserErrorCommand<Void> createSuccess() {
        return new UserErrorCommand<>(userCommandType, userInput);
    }
}
