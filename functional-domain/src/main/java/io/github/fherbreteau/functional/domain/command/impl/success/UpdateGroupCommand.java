package io.github.fherbreteau.functional.domain.command.impl.success;

import static java.util.Objects.nonNull;

import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public class UpdateGroupCommand extends AbstractModifyUserCommand<Group> {
    private final String name;
    private final UUID groupId;
    private final String newName;

    public UpdateGroupCommand(UserRepository userRepository, GroupRepository groupRepository,
                              UserUpdater userUpdater, String name, UUID groupId, String newName) {
        super(userRepository, groupRepository, userUpdater);
        this.name = name;
        this.groupId = groupId;
        this.newName = newName;
    }

    @Override
    public Output<Group> execute(User actor) {
        Group group = groupRepository.findByName(name);
        Group.Builder builder = group.copy();
        if (nonNull(groupId)) {
            builder.withGroupId(groupId);
        }
        if (nonNull(newName)) {
            builder.withName(newName);
        }
        Group newGroup = groupRepository.update(userUpdater.updateGroup(group, builder.build()));
        return Output.success(newGroup);
    }
}
