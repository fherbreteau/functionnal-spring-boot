package io.github.fherbreteau.functional.domain.command.impl.success;

import static io.github.fherbreteau.functional.domain.Logging.debug;
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
            debug(logger,  "Get group of user with name {0}", name);
            return Output.success(userRepository.findByName(name).getGroups());
        }
        if (nonNull(userId)) {
            debug(logger,  "Get group of user with id {0}", userId);
            return Output.success(userRepository.findById(userId).getGroups());
        }
        debug(logger,  "Get group of user {0}", actor);
        return Output.success(actor.getGroups());
    }
}
