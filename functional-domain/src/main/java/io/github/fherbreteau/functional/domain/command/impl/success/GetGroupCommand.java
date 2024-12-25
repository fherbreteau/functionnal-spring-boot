package io.github.fherbreteau.functional.domain.command.impl.success;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;

public class GetGroupCommand extends AbstractSuccessUserCommand<List<Group>> {
    private final String name;
    private final UUID userId;

    public GetGroupCommand(UserRepository userRepository, GroupRepository groupRepository, String name, UUID userId) {
        super(userRepository, groupRepository);
        this.name = name;
        this.userId = userId;
    }

    @Override
    public Output<List<Group>> execute(User actor) {
        if (nonNull(name)) {
            return Output.success(userRepository.findByName(name).getGroups());
        }
        if (nonNull(userId)) {
            return Output.success(userRepository.findById(userId).getGroups());
        }
        return Output.success(actor.getGroups());
    }
}
