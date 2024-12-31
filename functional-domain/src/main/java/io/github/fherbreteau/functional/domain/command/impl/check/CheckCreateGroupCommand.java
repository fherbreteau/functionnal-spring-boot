package io.github.fherbreteau.functional.domain.command.impl.check;

import static io.github.fherbreteau.functional.domain.Logging.debug;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.CreateGroupCommand;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public class CheckCreateGroupCommand extends AbstractCheckUserCommand<Group, CreateGroupCommand> {
    private final String name;
    private final UUID groupId;

    public CheckCreateGroupCommand(UserRepository userRepository, GroupRepository groupRepository, UserChecker userChecker,
                                   UserUpdater userUpdater, String name, UUID groupId) {
        super(userRepository, groupRepository, userChecker, userUpdater);
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
    protected CreateGroupCommand createSuccess() {
        debug(logger,  "Creating execute command");
        return new CreateGroupCommand(userRepository, groupRepository, userUpdater, name, groupId);
    }

    @Override
    protected UserErrorCommand<Group> createError(List<String> reasons) {
        UserInput userInput = UserInput.builder(name).withGroupId(groupId).build();
        return new UserErrorCommand<>(UserCommandType.GROUPADD, userInput, reasons);
    }
}
