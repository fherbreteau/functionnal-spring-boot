package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.GroupAddCommand;
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

import static java.util.Objects.nonNull;

public class CheckGroupAddCommand extends AbstractCheckUserCommand<GroupAddCommand> {
    private final String name;
    private final UUID groupId;

    public CheckGroupAddCommand(UserRepository userRepository, GroupRepository groupRepository, UserChecker userChecker,
                                PasswordProtector passwordProtector, String name, UUID groupId) {
        super(userRepository, groupRepository, userChecker, passwordProtector);
        this.name = name;
        this.groupId = groupId;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (!userChecker.canCreateGroup(name, actor)) {
            reasons.add(String.format("%s can't create group %s", actor, name));
        }
        if (groupRepository.exists(name)) {
            reasons.add(String.format("group %s already exists", groupId));
        }
        if (nonNull(groupId) && groupRepository.exists(groupId)) {
            reasons.add(String.format("a group with id %s already exists", groupId));
        }
        return reasons;
    }

    @Override
    protected GroupAddCommand createSuccess() {
        return new GroupAddCommand(userRepository, groupRepository, passwordProtector, name, groupId);
    }

    @Override
    protected UserErrorCommand createError(List<String> reasons) {
        UserInput userInput = UserInput.builder(name).withGroupId(groupId).build();
        return new UserErrorCommand(UserCommandType.GROUPADD, userInput, reasons);
    }
}
