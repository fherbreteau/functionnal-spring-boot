package io.github.fherbreteau.functional.domain.user;

import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserRepository;

import static java.lang.String.format;

public class UserManager {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public UserManager(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public Output findUserByName(String name) {
        if (userRepository.exists(name)) {
            return new Output(userRepository.findByName(name));
        } else {
            return new Output(Error.error(format("%s not found", name)));
        }
    }

    public Output findGroupByName(String name) {
        if (groupRepository.exists(name)) {
            return new Output(groupRepository.findByName(name));
        } else {
            return new Output(Error.error(format("%s not found", name)));
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
