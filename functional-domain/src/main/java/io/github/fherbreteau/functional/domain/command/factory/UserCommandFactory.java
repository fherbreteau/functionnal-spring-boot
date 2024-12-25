package io.github.fherbreteau.functional.domain.command.factory;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public interface UserCommandFactory<T> {

    boolean supports(UserCommandType type, UserInput userInput);

    CheckCommand<T> createCommand(UserRepository repository, GroupRepository groupRepository,
                                  UserChecker userChecker, UserUpdater userUpdater,
                                  PasswordProtector passwordProtector, UserCommandType type, UserInput userInput);

    default int order() {
        return 0;
    }
}
