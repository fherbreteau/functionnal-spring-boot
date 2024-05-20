package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.GroupDeleteCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

public class CheckGroupDeleteCommand extends AbstractCheckUserCommand<GroupDeleteCommand> {
    private final String name;
    private final boolean force;

    public CheckGroupDeleteCommand(UserRepository userRepository, GroupRepository groupRepository, UserChecker userChecker, String name, boolean force) {
        super(userRepository, groupRepository, userChecker);
        this.name = name;
        this.force = force;
    }

    @Override
    protected boolean checkAccess(User actor) {
        if (!userChecker.canDeleteGroup(name, actor)) {
            return false;
        }
        if (!groupRepository.exists(name)) {
            return false;
        }
        if (userRepository.hasUserWithGroup(name)) {
            return force;
        }
        return true;
    }

    @Override
    protected GroupDeleteCommand createSuccess() {
        return new GroupDeleteCommand(userRepository, groupRepository, name, force);
    }

    @Override
    protected UserErrorCommand createError() {
        UserInput userInput = UserInput.builder(name).withForce(force).build();
        return new UserErrorCommand(UserCommandType.GROUPDEL, userInput);
    }
}
