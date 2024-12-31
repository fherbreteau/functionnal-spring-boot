package io.github.fherbreteau.functional.domain.command.impl.check;

import static java.lang.System.Logger.Level.DEBUG;

import java.util.ArrayList;
import java.util.List;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.DeleteUserCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public class CheckDeleteUserCommand extends AbstractCheckUserCommand<Void, DeleteUserCommand> {
    private final String name;

    public CheckDeleteUserCommand(UserRepository userRepository, GroupRepository groupRepository,
                                  UserChecker userChecker, UserUpdater userUpdater, String name) {
        super(userRepository, groupRepository, userChecker, userUpdater);
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
    protected DeleteUserCommand createSuccess() {
        logger.log(DEBUG, "Creating execute command");
        return new DeleteUserCommand(userRepository, groupRepository, userUpdater, name);
    }

    @Override
    protected UserErrorCommand<Void> createError(List<String> reasons) {
        UserInput userInput = UserInput.builder(name).build();
        return new UserErrorCommand<>(UserCommandType.USERDEL, userInput, reasons);
    }
}
