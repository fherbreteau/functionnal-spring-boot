package io.github.fherbreteau.functional.domain.command.factory;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.*;

public interface UserCommandFactory {

    boolean supports(UserCommandType type, UserInput userInput);

    CheckCommand<Output> createCommand(UserRepository repository, GroupRepository groupRepository,
                                       UserChecker userChecker, PasswordProtector passwordProtector, UserCommandType type, UserInput userInput);

    default int order() {
        return 0;
    }
}
