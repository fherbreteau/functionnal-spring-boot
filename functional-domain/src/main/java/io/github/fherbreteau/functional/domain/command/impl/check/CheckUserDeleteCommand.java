package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.UserDeleteCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class CheckUserDeleteCommand extends AbstractCheckUserCommand<UserDeleteCommand> {
    private final String name;

    public CheckUserDeleteCommand(UserRepository userRepository, GroupRepository groupRepository,
                                  UserChecker userChecker, PasswordProtector passwordProtector, String name) {
        super(userRepository, groupRepository, userChecker, passwordProtector);
        this.name = name;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (!userChecker.canDeleteUser(name, actor)) {
            reasons.add(String.format("%s can't delete %s", actor, name));
        }
        if (!userRepository.exists(name)) {
            reasons.add(String.format("%s is missing", name));
        }
        return reasons;
    }

    @Override
    protected UserDeleteCommand createSuccess() {
        return new UserDeleteCommand(userRepository, groupRepository, passwordProtector, name);
    }

    @Override
    protected UserErrorCommand createError(List<String> reasons) {
        UserInput userInput = UserInput.builder(name).build();
        return new UserErrorCommand(UserCommandType.USERDEL, userInput, reasons);
    }
}
