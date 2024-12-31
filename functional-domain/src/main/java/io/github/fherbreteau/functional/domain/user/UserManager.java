package io.github.fherbreteau.functional.domain.user;

import static io.github.fherbreteau.functional.domain.Logging.debug;
import static java.lang.String.format;

import java.util.Objects;
import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;

public class UserManager {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public UserManager(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public Output<User> findUserByName(String name) {
        debug(logger, "Finding user with name {1}", name);
        if (userRepository.exists(name)) {
            return Output.success(userRepository.findByName(name));
        } else {
            return Output.failure(format("%s not found", name));
        }
    }

    public Output<Group> findGroupByName(String name) {
        debug(logger, "Finding group with name {1}", name);
        if (groupRepository.exists(name)) {
            return Output.success(groupRepository.findByName(name));
        } else {
            return Output.failure(format("%s not found", name));
        }
    }

    public Output<String> getPassword(User user) {
        debug(logger, "Getting password for {1}", user);
        if (userRepository.exists(user.getUserId())) {
            String password = userRepository.getPassword(user);
            if (Objects.nonNull(password) && !password.isEmpty()) {
                return Output.success(password);
            }
            return Output.failure(format("Password for user %s not found", user.getName()));
        }
        return Output.failure(format("User %s not found", user.getName()));
    }
}
