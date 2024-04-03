package io.github.fherbreteau.functional.domain.command;

import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.Error;

public interface Command<R> {

    boolean canExecute(User actor);

    R execute(User actor);

    Error handleError(User actor);
}
