package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserRepository;
import io.github.fherbreteau.functional.driven.UserUpdater;

public class DeleteGroupCommand extends AbstractModifyUserCommand {
    private final String name;
    private final boolean force;

    public DeleteGroupCommand(UserRepository userRepository, GroupRepository groupRepository,
                              UserUpdater userUpdater, String name, boolean force) {
        super(userRepository, groupRepository, userUpdater);
        this.name = name;
        this.force = force;
    }

    @Override
    public Output execute(User actor) {
        Group group = groupRepository.findByName(name);
        if (force) {
            group = userRepository.removeGroupFromUser(group);
        }
        return new Output(groupRepository.delete(userUpdater.deleteGroup(group)));
    }
}
