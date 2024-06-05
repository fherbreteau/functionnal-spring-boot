package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.driven.ItemRepository;

public abstract class AbstractSuccessItemCommand<T> implements Command<Output<T>> {

    protected final ItemRepository repository;

    protected AbstractSuccessItemCommand(ItemRepository repository) {
        this.repository = repository;
    }
}
