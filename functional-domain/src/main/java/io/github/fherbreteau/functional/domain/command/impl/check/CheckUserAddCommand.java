package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.UserAddCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.UUID;

import static java.util.Objects.nonNull;

public class CheckUserAddCommand extends AbstractCheckUserCommand<UserAddCommand> {
    private final String name;
    private final UUID userId;
    private final UUID groupId;
    private final String password;

    public CheckUserAddCommand(UserRepository userRepository, GroupRepository groupRepository, UserChecker userChecker,
                               String name, UUID userId, UUID groupId, String password) {
        super(userRepository, groupRepository, userChecker);
        this.name = name;
        this.userId = userId;
        this.groupId = groupId;
        this.password = password;
    }

    @Override
    protected boolean checkAccess(User actor) {
        if (!userChecker.canCreateUser(name, actor)) {
            return false;
        }
        if (userRepository.exists(name)) {
            return false;
        }
        if (nonNull(userId) && userRepository.exists(userId)) {
            return false;
        }
        if (nonNull(groupId) && !groupRepository.exists(groupId)) {
            return false;
        }
        return !groupRepository.exists(name);
    }

    @Override
    protected UserAddCommand createSuccess() {
        return new UserAddCommand(userRepository, groupRepository, name, userId, groupId, password);
    }

    @Override
    protected UserErrorCommand createError() {
        UserInput userInput = UserInput.builder(name)
                .withUserId(userId)
                .withGroupId(groupId)
                .withPassword(password)
                .build();
        return new UserErrorCommand(UserCommandType.USERADD, userInput);
    }
}
