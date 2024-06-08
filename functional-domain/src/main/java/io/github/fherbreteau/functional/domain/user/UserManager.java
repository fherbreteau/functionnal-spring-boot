package io.github.fherbreteau.functional.domain.user;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;

import static java.lang.String.format;

public class UserManager {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public UserManager(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public Output<User> findUserByName(String name) {
        if (userRepository.exists(name)) {
            return Output.success(userRepository.findByName(name));
        } else {
            return Output.error(format("%s not found", name));
        }
    }

    public Output<Group> findGroupByName(String name) {
        if (groupRepository.exists(name)) {
            return Output.success(groupRepository.findByName(name));
        } else {
            return Output.error(format("%s not found", name));
        }
    }

    public boolean checkPassword(String name, String passwordHash) {
        if (userRepository.exists(name)) {
            User user = userRepository.findByName(name);
            return userRepository.checkPassword(user, passwordHash);
        } else {
            return false;
        }
    }
}
