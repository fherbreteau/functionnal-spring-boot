package io.github.fherbreteau.functional.domain.command.impl.success;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public class UpdateUserCommand extends AbstractModifyUserCommand<User> {
    private final PasswordProtector passwordProtector;
    private final String name;
    private final UUID userId;
    private final String newName;
    private final String password;
    private final UUID groupId;
    private final List<String> groups;
    private final boolean append;

    public UpdateUserCommand(UserRepository userRepository, GroupRepository groupRepository,
                             UserUpdater userUpdater, PasswordProtector passwordProtector, UserInput input) {
        super(userRepository, groupRepository, userUpdater);
        this.passwordProtector = passwordProtector;
        this.name = input.getName();
        this.userId = input.getUserId();
        this.newName = input.getNewName();
        this.password = input.getPassword();
        this.groupId = input.getGroupId();
        this.groups = input.getGroups();
        this.append = input.isAppend();
    }

    @Override
    public Output<User> execute(User actor) {
        logger.debug("Updating user with name {}", name);
        User user = userRepository.findByName(name);
        User.Builder builder = user.copy();
        if (nonNull(userId)) {
            builder.withUserId(userId);
        }
        if (nonNull(newName)) {
            builder.withName(newName);
        }
        if (nonNull(groupId)) {
            Group group = groupRepository.findById(groupId);
            builder.withGroup(group);
        }
        if (!groups.isEmpty()) {
            List<Group> newGroups = groups.stream().map(groupRepository::findByName).toList();
            if (append) {
                builder.addGroups(newGroups);
            } else {
                builder.withGroups(newGroups);
            }
        }
        User newUser = userRepository.update(userUpdater.updateUser(user, builder.build()));
        if (nonNull(password)) {
            newUser = userRepository.updatePassword(newUser, passwordProtector.protect(password));
        }
        return Output.success(newUser);
    }
}
