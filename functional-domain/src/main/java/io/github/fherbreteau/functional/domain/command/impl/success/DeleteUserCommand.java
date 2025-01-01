package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public class DeleteUserCommand extends AbstractModifyUserCommand<Void> {
    private final String name;

    public DeleteUserCommand(UserRepository userRepository, GroupRepository groupRepository,
                             UserUpdater userUpdater, String name) {
        super(userRepository, groupRepository, userUpdater);
        this.name = name;
    }

    @Override
    public Output<Void> execute(User actor) {
        logger.debug("Deleting user with name {}", name);
        User user = userRepository.findByName(name);
        userUpdater.deleteUser(user);
        userRepository.delete(user);
        return Output.success(null);
    }
}
