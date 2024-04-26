package io.github.fherbreteau.functional.domain.command.impl.error;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.command.Output;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.User;

public class ErrorCommand implements Command<Output> {

    private final CommandType type;

    private final Input input;

    public ErrorCommand(CommandType type, Input input) {
        this.type = type;
        this.input = input;
    }

    @Override
    public Output execute(User actor) {
        return new Output(new Error(type, input, actor));
    }
}
