package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.exception.UnsupportedCommandException;

public class UnsupportedCommand implements Command<Void> {
    private final CommandType type;
    private final Input input;

    public UnsupportedCommand(CommandType type, Input input) {
        this.type = type;
        this.input = input;
    }

    @Override
    public boolean canExecute(User actor) {
        return false;
    }

    @Override
    public Void execute(User actor) {
        throw new UnsupportedCommandException(type, input, actor);
    }

    @Override
    public Error handleError(User actor) {
        return new Error(type, input, actor);
    }
}
