package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public class DeleteGroupCommand extends AbstractModifyUserCommand<Void> {
    private final String name;
    private final boolean force;

    public DeleteGroupCommand(UserRepository userRepository, GroupRepository groupRepository,
                              UserUpdater userUpdater, String name, boolean force) {
        super(userRepository, groupRepository, userUpdater);
        this.name = name;
        this.force = force;
    }

    @Override
    public Output<Void> execute(User actor) {
        logger.debug("Deleting group with name {}", name);
        Group group = groupRepository.findByName(name);
        if (force) {
            userRepository.removeGroupFromUser(group);
        }
        userUpdater.deleteGroup(group);
        groupRepository.delete(group);
        return Output.success(null);
    }
}
