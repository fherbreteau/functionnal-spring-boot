package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class UserAddCommand extends AbstractSuccessUserCommand {
    private final String name;
    private final UUID userId;
    private final UUID groupId;
    private final List<String> groups;
    private final String password;

    public UserAddCommand(UserRepository userRepository, GroupRepository groupRepository,
                          PasswordProtector passwordProtector, UserInput input) {
        super(userRepository, groupRepository, passwordProtector);
        this.name = input.getName();
        this.userId = input.getUserId();
        this.groupId = input.getGroupId();
        this.groups = input.getGroups();
        this.password = input.getPassword();
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
        }
        if (!groups.isEmpty()) {
            List<Group> newGroups = groups.stream().map(groupRepository::findByName).toList();
            builder.addGroups(newGroups);
        }
        // Use user's name as group's name, if none defined.
        if (isNull(groupId) && groups.isEmpty()) {
            Group group = groupRepository.save(Group.builder(name).build());
            builder.withGroup(group);
        }
        User user = userRepository.save(builder.build());
        if (nonNull(password)) {
            userRepository.updatePassword(user, passwordProtector.protect(password));
        }
        return new Output(user);
    }
}
