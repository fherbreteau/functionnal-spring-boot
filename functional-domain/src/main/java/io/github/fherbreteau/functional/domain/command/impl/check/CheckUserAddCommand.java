package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.UserAddCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class CheckUserAddCommand extends AbstractCheckUserCommand<UserAddCommand> {
    private final String name;
    private final UUID userId;
    private final UUID groupId;
    private final List<String> groups;
    private final String password;
    private final UserInput input;

    public CheckUserAddCommand(UserRepository userRepository, GroupRepository groupRepository, UserChecker userChecker,
                               PasswordProtector passwordProtector, UserInput input) {
        super(userRepository, groupRepository, userChecker, passwordProtector);
        this.name = input.getName();
        this.userId = input.getUserId();
        this.groupId = input.getGroupId();
        this.groups = input.getGroups();
        this.password = input.getPassword();
        this.input = input;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (!userChecker.canCreateUser(name, actor)) {
            reasons.add(String.format("%s can't create user %s", actor, name));
        }
        if (userRepository.exists(name)) {
            reasons.add(String.format("%s already exists", name));
        }
        if (nonNull(userId) && userRepository.exists(userId)) {
            reasons.add(String.format("a user with id %s already exists", userId));
        }
        if (nonNull(groupId) && !groupRepository.exists(groupId)) {
            reasons.add(String.format("%s is missing", groupId));
        }
        if (!groups.isEmpty() && groups.stream().anyMatch(g -> !groupRepository.exists(g))) {
            reasons.add(String.format("one of %s is missing", String.join(", ", groups)));
        }
        if (isNull(groupId) && groups.isEmpty() && groupRepository.exists(name)) {
            reasons.add(String.format("group %s already exists", name));
        }
        if (nonNull(password)) {
            reasons.addAll(passwordProtector.validate(password));
        }
        return reasons;
    }

    @Override
    protected UserAddCommand createSuccess() {
        return new UserAddCommand(userRepository, groupRepository, passwordProtector, input);
    }

    @Override
    protected UserErrorCommand createError(List<String> reasons) {
        return new UserErrorCommand(UserCommandType.USERADD, input, reasons);
    }
}
