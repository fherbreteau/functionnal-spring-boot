package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.GroupAddCommand;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.UUID;

import static java.util.Objects.isNull;

public class CheckGroupAddCommand extends AbstractCheckUserCommand<GroupAddCommand> {
    private final String name;
    private final UUID groupId;

    public CheckGroupAddCommand(UserRepository userRepository, GroupRepository groupRepository, UserChecker userChecker, String name, UUID groupId) {
        super(userRepository, groupRepository, userChecker);
        this.name = name;
        this.groupId = groupId;
    }

    @Override
    protected boolean checkAccess(User actor) {
        if (!userChecker.canCreateGroup(name, actor)) {
            return false;
        }
        if (groupRepository.exists(name)) {
            return false;
        }
        return isNull(groupId) || !groupRepository.exists(groupId);
    }

    @Override
    protected GroupAddCommand createSuccess() {
        return new GroupAddCommand(userRepository, groupRepository, name, groupId);
    }

    @Override
    protected UserErrorCommand createError() {
        UserInput userInput = UserInput.builder(name).withGroupId(groupId).build();
        return new UserErrorCommand(UserCommandType.USERADD, userInput);
    }
}
