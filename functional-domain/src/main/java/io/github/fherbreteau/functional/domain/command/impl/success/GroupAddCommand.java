package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.UUID;

import static java.util.Objects.nonNull;

public class GroupAddCommand extends AbstractSuccessUserCommand {
    private final String name;
    private final UUID groupId;

    public GroupAddCommand(UserRepository userRepository, GroupRepository groupRepository,
                           PasswordProtector passwordProtector, String name, UUID groupId) {
        super(userRepository, groupRepository, passwordProtector);
        this.name = name;
        this.groupId = groupId;
    }

    @Override
    public Output execute(User actor) {
        Group.Builder builder = Group.builder(name);
        if (nonNull(groupId)) {
            builder.withGroupId(groupId);
        }
        Group group = groupRepository.save(builder.build());
        return new Output(group);
    }
}
