package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.UserDeleteCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

public class CheckUserDeleteCommand extends AbstractCheckUserCommand<UserDeleteCommand> {
    private final String name;

    public CheckUserDeleteCommand(UserRepository userRepository, GroupRepository groupRepository, UserChecker userChecker, String name) {
        super(userRepository, groupRepository, userChecker);
        this.name = name;
    }

    @Override
    protected boolean checkAccess(User actor) {
        if (!userChecker.canDeleteUser(name, actor)) {
            return false;
        }
        return userRepository.exists(name);
    }

    @Override
    protected UserDeleteCommand createSuccess() {
        return new UserDeleteCommand(userRepository, groupRepository, name);
    }

    @Override
    protected UserErrorCommand createError() {
        UserInput userInput = UserInput.builder(name).build();
        return new UserErrorCommand(UserCommandType.USERDEL, userInput);
    }
}
