package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserRepository;
import io.github.fherbreteau.functional.driven.UserUpdater;

import java.util.UUID;

import static java.util.Objects.nonNull;

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
        Group newGroup = groupRepository.save(userUpdater.updateGroup(group, builder.build()));
        return Output.success(newGroup);
    }
}
