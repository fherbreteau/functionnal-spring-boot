package io.github.fherbreteau.functional.domain.command.impl.error;

import java.util.List;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;

public class UserErrorCommand<T> implements Command<Output<T>> {

    private final UserCommandType type;
    private final UserInput userInput;
    private final List<String> reasons;

    public UserErrorCommand(UserCommandType type, UserInput userInput) {
        this(type, userInput, List.of());
    }

    public UserErrorCommand(UserCommandType type, UserInput userInput, List<String> reasons) {
        this.type = type;
        this.userInput = userInput;
        this.reasons = reasons;
    }

    @Override
    public Output<T> execute(User actor) {
        return Output.failure(String.format("%s with arguments %s failed for %s", type, userInput, actor), reasons);
    }
}
