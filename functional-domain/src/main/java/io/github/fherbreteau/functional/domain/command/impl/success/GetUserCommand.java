package io.github.fherbreteau.functional.domain.command.impl.success;

import static java.util.Objects.nonNull;

import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;

public class GetUserCommand extends AbstractSuccessUserCommand<User> {
    private final String name;
    private final UUID userId;

    public GetUserCommand(UserRepository userRepository, GroupRepository groupRepository, String name, UUID userId) {
        super(userRepository, groupRepository);
        this.name = name;
        this.userId = userId;
    }

    @Override
    public Output<User> execute(User actor) {
        if (nonNull(name)) {
            return Output.success(userRepository.findByName(name));
        }
        if (nonNull(userId)) {
            return Output.success(userRepository.findById(userId));
        }
        return Output.success(actor);
    }
}
