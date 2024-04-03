package io.github.fherbreteau.functional.domain.command;

import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.User;

public interface Command<R> {

    boolean canExecute(User actor);

    R execute(User actor);

    Error handleError(User actor);
}
