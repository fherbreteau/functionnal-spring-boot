package io.github.fherbreteau.functional.domain.command.impl.check;

import static java.lang.System.Logger.Level.DEBUG;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.UpdateGroupCommand;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public class CheckUpdateGroupCommand extends AbstractCheckUserCommand<Group, UpdateGroupCommand> {
    private final String name;
    private final UUID groupId;
    private final String newName;

    public CheckUpdateGroupCommand(UserRepository userRepository, GroupRepository groupRepository,
                                   UserChecker userChecker, UserUpdater userUpdater, String name,
                                   UUID groupId, String newName) {
        super(userRepository, groupRepository, userChecker, userUpdater);
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
    protected UpdateGroupCommand createSuccess() {
        logger.log(DEBUG, "Creating execute command");
        return new UpdateGroupCommand(userRepository, groupRepository, userUpdater, name, groupId, newName);
    }

    @Override
    protected UserErrorCommand<Group> createError(List<String> reasons) {
        UserInput userInput = UserInput.builder(name).withGroupId(groupId).withNewName(newName).build();
        return new UserErrorCommand<>(UserCommandType.GROUPMOD, userInput, reasons);
    }
}
