package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.GroupDeleteCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class CheckGroupDeleteCommand extends AbstractCheckUserCommand<GroupDeleteCommand> {
    private final String name;
    private final boolean force;

    public CheckGroupDeleteCommand(UserRepository userRepository, GroupRepository groupRepository,
                                   UserChecker userChecker, PasswordProtector passwordProtector,
                                   String name, boolean force) {
        super(userRepository, groupRepository, userChecker, passwordProtector);
        this.name = name;
        this.force = force;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (!userChecker.canDeleteGroup(name, actor)) {
            reasons.add(String.format("%s can't delete group %s", actor, name));
        }
        if (!groupRepository.exists(name)) {
            reasons.add(String.format("%s is missing", name));
        }
        if (!force && userRepository.hasUserWithGroup(name)) {
            reasons.add(String.format("%s still contain users", name));
        }
        return reasons;
    }

    @Override
    protected GroupDeleteCommand createSuccess() {
        return new GroupDeleteCommand(userRepository, groupRepository, passwordProtector, name, force);
    }

    @Override
    protected UserErrorCommand createError(List<String> reasons) {
        UserInput userInput = UserInput.builder(name).withForce(force).build();
        return new UserErrorCommand(UserCommandType.GROUPDEL, userInput, reasons);
    }
}
