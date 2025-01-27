package io.github.fherbreteau.functional.domain.command.impl.success;

import static java.util.Objects.nonNull;

import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public class CreateGroupCommand extends AbstractModifyUserCommand<Group> {
    private final String name;
    private final UUID groupId;

    public CreateGroupCommand(UserRepository userRepository, GroupRepository groupRepository,
                              UserUpdater userUpdater, String name, UUID groupId) {
        super(userRepository, groupRepository, userUpdater);
        this.name = name;
        this.groupId = groupId;
    }

    @Override
    public Output<Group> execute(User actor) {
        logger.debug("Creating group with name {} and id {}", name, groupId);
        Group.Builder builder = Group.builder(name);
        if (nonNull(groupId)) {
            builder.withGroupId(groupId);
        }
        Group group = groupRepository.create(userUpdater.createGroup(builder.build()));
        return Output.success(group);
    }
}
