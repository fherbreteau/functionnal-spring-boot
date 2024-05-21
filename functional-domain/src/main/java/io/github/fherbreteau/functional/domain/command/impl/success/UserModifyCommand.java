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

import static java.util.Objects.nonNull;

public class UserModifyCommand extends AbstractSuccessUserCommand {
    private final String name;
    private final UUID userId;
    private final String newName;
    private final String password;
    private final UUID groupId;
    private final List<String> groups;
    private final boolean append;

    public UserModifyCommand(UserRepository userRepository, GroupRepository groupRepository,
                             PasswordProtector passwordProtector, UserInput input) {
        super(userRepository, groupRepository, passwordProtector);
        this.name = input.getName();
        this.userId = input.getUserId();
        this.newName = input.getNewName();
        this.password = input.getPassword();
        this.groupId = input.getGroupId();
        this.groups = input.getGroups();
        this.append = input.isAppend();
    }

    @Override
    public Output execute(User actor) {
        User user = userRepository.findByName(name);
        if (nonNull(userId)) {
            user = user.withUserId(userId);
        }
        if (nonNull(newName)) {
            user = user.withName(newName);
        }
        if (nonNull(groupId)) {
            Group group = groupRepository.findById(groupId);
            user = user.withGroup(group);
        }
        if (!groups.isEmpty()) {
            List<Group> newGroups = groups.stream().map(groupRepository::findByName).toList();
            if (append) {
                user = user.addGroups(newGroups);
            } else {
                user = user.withGroups(newGroups);
            }
        }
        user = userRepository.save(user);
        if (nonNull(password)) {
            user = userRepository.updatePassword(user, passwordProtector.protect(password));
        }
        return new Output(user);
    }
}
