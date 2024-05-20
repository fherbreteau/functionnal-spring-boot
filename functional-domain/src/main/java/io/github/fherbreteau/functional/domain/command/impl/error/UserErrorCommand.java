package io.github.fherbreteau.functional.domain.command.impl.error;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.*;

public class UserErrorCommand implements Command<Output> {

    private final UserCommandType type;

    private final UserInput userInput;

    public UserErrorCommand(UserCommandType type, UserInput userInput) {
        this.type = type;
        this.userInput = userInput;
    }

    @Override
    public Output execute(User actor) {
        return new Output(Error.error(String.format("%s with arguments %s failed for %s", type, userInput, actor)));
    }
}
