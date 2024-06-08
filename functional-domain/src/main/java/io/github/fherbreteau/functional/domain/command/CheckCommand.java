package io.github.fherbreteau.functional.domain.command;

import io.github.fherbreteau.functional.domain.entities.Output;

public interface CheckCommand<T> extends Command<Command<Output<T>>> {
}
