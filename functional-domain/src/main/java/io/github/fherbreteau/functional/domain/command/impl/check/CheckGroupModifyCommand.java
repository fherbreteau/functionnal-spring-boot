package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.GroupModifyCommand;
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

public class CheckGroupModifyCommand extends AbstractCheckUserCommand<GroupModifyCommand> {
    private final String name;
    private final UUID groupId;
    private final String newName;

    public CheckGroupModifyCommand(UserRepository userRepository, GroupRepository groupRepository,
                                   UserChecker userChecker, PasswordProtector passwordProtector, String name,
                                   UUID groupId, String newName) {
        super(userRepository, groupRepository, userChecker, passwordProtector);
        this.name = name;
        this.groupId = groupId;
        this.newName = newName;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (!userChecker.canUpdateGroup(name, actor)) {
            reasons.add(String.format("%s can't update group %s", actor, name));
        }
        if (!groupRepository.exists(name)) {
            reasons.add(String.format("group %s is missing", name));
        }
        if (nonNull(groupId) && groupRepository.exists(groupId)) {
            reasons.add(String.format("group with id %s already exists", groupId));
        }
        if (nonNull(newName) && groupRepository.exists(newName)) {
            reasons.add(String.format("group %s already exists", groupId));
        }
        return reasons;
    }

    @Override
    protected GroupModifyCommand createSuccess() {
        return new GroupModifyCommand(userRepository, groupRepository, passwordProtector, name, groupId, newName);
    }

    @Override
    protected UserErrorCommand createError(List<String> reasons) {
        UserInput userInput = UserInput.builder(name).withGroupId(groupId).withNewName(newName).build();
        return new UserErrorCommand(UserCommandType.GROUPMOD, userInput, reasons);
    }
}
