package io.github.fherbreteau.functional.domain.exception;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.entities.User;

public class UnsupportedCommandException extends RuntimeException {
    public UnsupportedCommandException(CommandType type, Input input, User actor) {
        super(String.format("%s with arguments %s failed for %s", type, input, actor));
    }
}
