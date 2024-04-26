package io.github.fherbreteau.functional.domain.command;

import io.github.fherbreteau.functional.domain.entities.User;

public interface Command<R> {

    R execute(User actor);
}
