package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.Output;
import io.github.fherbreteau.functional.driven.FileRepository;

public abstract class AbstractSuccessCommand implements Command<Output> {

    protected final FileRepository repository;

    protected AbstractSuccessCommand(FileRepository repository) {
        this.repository = repository;
    }
}
