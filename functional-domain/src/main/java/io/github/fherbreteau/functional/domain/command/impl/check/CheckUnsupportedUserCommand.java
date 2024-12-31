package io.github.fherbreteau.functional.domain.command.impl.check;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.List;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

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
        debug(logger,  "Creating error command");
        return new UserErrorCommand<>(userCommandType, userInput);
    }
}
