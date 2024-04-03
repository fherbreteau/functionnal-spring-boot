package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public abstract class AbstractCreateCommand<I extends Item<?, ?>> extends AbstractCommand<I> {

    protected final String name;

    protected final Folder parent;

    protected AbstractCreateCommand(FileRepository repository, AccessChecker accessChecker, String name, Folder parent) {
        super(repository, accessChecker);
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public Folder getParent() {
        return parent;
    }

}
