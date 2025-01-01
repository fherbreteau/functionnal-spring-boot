package io.github.fherbreteau.functional.domain.command.impl.check;

import java.util.ArrayList;
import java.util.List;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.DeleteGroupCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public class CheckDeleteGroupCommand extends AbstractCheckUserCommand<Void, DeleteGroupCommand> {
    private final String name;
    private final boolean force;

    public CheckDeleteGroupCommand(UserRepository userRepository, GroupRepository groupRepository,
                                   UserChecker userChecker, UserUpdater userUpdater,
                                   String name, boolean force) {
        super(userRepository, groupRepository, userChecker, userUpdater);
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
    protected DeleteGroupCommand createSuccess() {
        logger.debug("Creating execute command");
        return new DeleteGroupCommand(userRepository, groupRepository, userUpdater, name, force);
    }

    @Override
    protected UserErrorCommand<Void> createError(List<String> reasons) {
        UserInput userInput = UserInput.builder(name).withForce(force).build();
        return new UserErrorCommand<>(UserCommandType.GROUPDEL, userInput, reasons);
    }
}
