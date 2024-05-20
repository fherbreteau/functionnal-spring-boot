package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserRepository;

public class UserDeleteCommand extends AbstractSuccessUserCommand {
    private final String name;

    public UserDeleteCommand(UserRepository userRepository, GroupRepository groupRepository, String name) {
        super(userRepository, groupRepository);
        this.name = name;
    }

    @Override
    public Output execute(User actor) {
        User user = userRepository.findByName(name);
        user = userRepository.delete(user);
        return new Output(user);
    }
}
