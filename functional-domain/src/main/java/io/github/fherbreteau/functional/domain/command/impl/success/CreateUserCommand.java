package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class CreateUserCommand extends AbstractModifyUserCommand<User> {
    private final PasswordProtector passwordProtector;
    private final String name;
    private final UUID userId;
    private final UUID groupId;
    private final List<String> groups;
    private final String password;

    public CreateUserCommand(UserRepository userRepository, GroupRepository groupRepository,
                             UserUpdater userUpdater, PasswordProtector passwordProtector, UserInput input) {
        super(userRepository, groupRepository, userUpdater);
        this.passwordProtector = passwordProtector;
        this.name = input.getName();
        this.userId = input.getUserId();
        this.groupId = input.getGroupId();
        this.groups = input.getGroups();
        this.password = input.getPassword();
    }

    @Override
    public Output<User> execute(User actor) {
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
            Group group = groupRepository.create(userUpdater.createGroup(Group.builder(name).build()));
            builder.withGroup(group);
        }
        User user = userRepository.create(userUpdater.createUser(builder.build()));
        if (nonNull(password)) {
            userRepository.updatePassword(user, passwordProtector.protect(password));
        }
        return Output.success(user);
    }
}
