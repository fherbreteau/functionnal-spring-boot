package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserRepository;
import io.github.fherbreteau.functional.driven.UserUpdater;

public class DeleteUserCommand extends AbstractModifyUserCommand<Void> {
    private final String name;

    public DeleteUserCommand(UserRepository userRepository, GroupRepository groupRepository,
                             UserUpdater userUpdater, String name) {
        super(userRepository, groupRepository, userUpdater);
        this.name = name;
    }

    @Override
    public Output<Void> execute(User actor) {
        User user = userRepository.findByName(name);
        userUpdater.deleteUser(user);
        userRepository.delete(user);
        return Output.success(null);
    }
}
