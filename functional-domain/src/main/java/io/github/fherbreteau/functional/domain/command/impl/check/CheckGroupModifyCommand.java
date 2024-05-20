package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.GroupModifyCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class CheckGroupModifyCommand extends AbstractCheckUserCommand<GroupModifyCommand> {
    private final String name;
    private final UUID groupId;
    private final String newName;

    public CheckGroupModifyCommand(UserRepository userRepository, GroupRepository groupRepository,
                                   UserChecker userChecker, String name, UUID groupId, String newName) {
        super(userRepository, groupRepository, userChecker);
        this.name = name;
        this.groupId = groupId;
        this.newName = newName;
    }

    @Override
    protected boolean checkAccess(User actor) {
        if (!userChecker.canUpdateGroup(name, actor)) {
            return false;
        }
        if (!groupRepository.exists(name)) {
            return false;
        }
        if (nonNull(groupId) && groupRepository.exists(groupId)) {
            return false;
        }
        return isNull(newName) || !groupRepository.exists(newName);
    }

    @Override
    protected GroupModifyCommand createSuccess() {
        return new GroupModifyCommand(userRepository, groupRepository, name, groupId, newName);
    }

    @Override
    protected UserErrorCommand createError() {
        UserInput userInput = UserInput.builder(name).withGroupId(groupId).withNewName(newName).build();
        return new UserErrorCommand(UserCommandType.GROUPMOD, userInput);
    }
}
