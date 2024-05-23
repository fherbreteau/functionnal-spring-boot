package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

public abstract class AbstractCheckGetInfoCommand<C extends Command<Output>> extends AbstractCheckUserCommand<C>  {
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
    protected final UserErrorCommand createError(List<String> reasons) {
        UserInput input = UserInput.builder(name).withUserId(userId).build();
        return new UserErrorCommand(type, input, reasons);
    }
}
