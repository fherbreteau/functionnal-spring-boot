package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.UUID;

import static java.util.Objects.nonNull;

public class GroupModifyCommand extends AbstractSuccessUserCommand {
    private final String name;
    private final UUID groupId;
    private final String newName;

    public GroupModifyCommand(UserRepository userRepository, GroupRepository groupRepository,
                              PasswordProtector passwordProtector, String name, UUID groupId, String newName) {
        super(userRepository, groupRepository, passwordProtector);
        this.name = name;
        this.groupId = groupId;
        this.newName = newName;
    }

    @Override
    public Output execute(User actor) {
        Group group = groupRepository.findByName(name);
        if (nonNull(groupId)) {
            group = group.withGroupId(groupId);
        }
        if (nonNull(newName)) {
            group = group.withName(newName);
        }
        group = groupRepository.save(group);
        return new Output(group);
    }
}
