package io.github.fherbreteau.functional.domain.user;

import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserRepository;
import io.github.fherbreteau.functional.exception.NotFoundException;

public class UserManager {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public UserManager(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public Output findUserByName(String name) {
        try {
            return new Output(userRepository.findByName(name));
        } catch (NotFoundException e) {
            return new Output(Error.error(e.getMessage()));
        }
    }

    public Output findGroupByName(String name) {
        try {
            return new Output(groupRepository.findByName(name));
        } catch (NotFoundException e) {
            return new Output(Error.error(e.getMessage()));
        }
    }

    public boolean checkPassword(String name, String passwordHash) {
        try {
            User user = userRepository.findByName(name);
            return userRepository.checkPassword(user, passwordHash);
        } catch (NotFoundException e) {
            return false;
        }
    }
}
