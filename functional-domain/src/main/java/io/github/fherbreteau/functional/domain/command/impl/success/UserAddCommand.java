package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.UUID;

import static java.util.Objects.nonNull;

public class UserAddCommand extends AbstractSuccessUserCommand {
    private final String name;
    private final UUID userId;
    private final UUID groupId;
    private final String password;

    public UserAddCommand(UserRepository userRepository, GroupRepository groupRepository, String name, UUID userId, UUID groupId, String password) {
        super(userRepository, groupRepository);
        this.name = name;
        this.userId = userId;
        this.groupId = groupId;
        this.password = password;
    }

    @Override
    public Output execute(User actor) {
        User.Builder builder = User.builder(name);
        if (nonNull(userId)) {
            builder.withUserId(userId);
        }
        if (nonNull(groupId)) {
            Group group = groupRepository.findById(groupId);
            builder.withGroup(group);
        } else {
            Group group = groupRepository.save(Group.builder(name).build());
            builder.withGroup(group);
        }
        User user = userRepository.save(builder.build());
        if (nonNull(password)) {
            userRepository.updatePassword(user, password);
        }
        return new Output(user);
    }
}
