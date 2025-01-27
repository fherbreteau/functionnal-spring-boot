package io.github.fherbreteau.functional.domain.command.impl.check;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public abstract class AbstractCheckGetInfoCommand<T, C extends Command<Output<T>>> extends AbstractCheckUserCommand<T, C> {
    protected final String name;
    protected final UUID userId;
    private final UserCommandType type;

    protected AbstractCheckGetInfoCommand(UserRepository userRepository, GroupRepository groupRepository,
                                          UserChecker userChecker, UserUpdater userUpdater, String name, UUID userId,
                                          UserCommandType type) {
        super(userRepository, groupRepository, userChecker, userUpdater);
        this.name = name;
        this.userId = userId;
        this.type = type;
    }

    @Override
    protected final List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (nonNull(name) && !userRepository.exists(name)) {
            reasons.add(String.format("%s is missing", name));
        }
        if (nonNull(userId) && !userRepository.exists(userId)) {
            reasons.add(String.format("%s is missing", userId));
        }
        return reasons;
    }

    @Override
    protected final UserErrorCommand<T> createError(List<String> reasons) {
        UserInput input = UserInput.builder(name).withUserId(userId).build();
        return new UserErrorCommand<>(type, input, reasons);
    }
}
