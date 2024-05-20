package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.UUID;

import static java.util.Objects.nonNull;

public class UserModifyCommand extends AbstractSuccessUserCommand {
    private final String name;
    private final UUID userId;
    private final String newName;
    private final String password;
    private final UUID groupId;
    private final String groupName;

    public UserModifyCommand(UserRepository userRepository, GroupRepository groupRepository, UserInput input) {
        super(userRepository, groupRepository);
        this.name = input.getName();
        this.userId = input.getUserId();
        this.newName = input.getNewName();
        this.password = input.getPassword();
        this.groupId = input.getGroupId();
        this.groupName = input.getGroupName();
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
        if (nonNull(groupId) && nonNull(groupName)) {
            Group group = groupRepository.findByNameAndId(groupName, groupId);
            user = user.withGroup(group);
        } else if (nonNull(groupId)) {
            Group group = groupRepository.findById(groupId);
            user = user.withGroup(group);
        } else if (nonNull(groupName)) {
            Group group = groupRepository.findByName(groupName);
            user = user.withGroup(group);
        }
        user = userRepository.save(user);
        if (nonNull(password)) {
            user = userRepository.updatePassword(user, password);
        }
        return new Output(user);
    }
}
