package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.UserModifyCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.UUID;

import static java.util.Objects.nonNull;

public class CheckUserModifyCommand extends AbstractCheckUserCommand<UserModifyCommand> {
    private final String name;
    private final UUID userId;
    private final String newName;
    private final String password;
    private final UUID groupId;
    private final String groupName;
    private final UserInput input;

    public CheckUserModifyCommand(UserRepository userRepository, GroupRepository groupRepository,
                                  UserChecker userChecker, UserInput input) {
        super(userRepository, groupRepository, userChecker);
        this.name = input.getName();
        this.userId = input.getUserId();
        this.newName = input.getNewName();
        this.password = input.getPassword();
        this.groupId = input.getGroupId();
        this.groupName = input.getGroupName();
        this.input = input;
    }

    @Override
    protected boolean checkAccess(User actor) {
        if (!userChecker.canUpdateUser(name, actor)) {
            return false;
        }
        if (!userRepository.exists(name)) {
            return false;
        }
        if (nonNull(userId) && userRepository.exists(userId)) {
            return false;
        }
        if (nonNull(newName) && userRepository.exists(newName)) {
            return false;
        }
        if (nonNull(groupId) && nonNull(groupName)) {
            return groupRepository.exists(groupId, groupName);
        }
        if (nonNull(groupId) && !groupRepository.exists(groupId)) {
            return false;
        }
        return !nonNull(groupName) || groupRepository.exists(groupName);
    }

    @Override
    protected UserModifyCommand createSuccess() {
        return new UserModifyCommand(userRepository, groupRepository, input);
    }

    @Override
    protected UserErrorCommand createError() {
        UserInput userInput = UserInput.builder(name)
                .withUserId(userId)
                .withNewName(newName)
                .withPassword(password)
                .withGroupId(groupId)
                .withGroupName(groupName)
                .build();
        return new UserErrorCommand(UserCommandType.USERMOD, userInput);
    }
}
