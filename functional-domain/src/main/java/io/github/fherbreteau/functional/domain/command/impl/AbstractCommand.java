package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public abstract class AbstractCommand<R> implements Command<R> {

    protected final FileRepository repository;

    protected final AccessChecker accessChecker;

    protected AbstractCommand(FileRepository repository, AccessChecker accessChecker) {
        this.repository = repository;
        this.accessChecker = accessChecker;
    }
}
