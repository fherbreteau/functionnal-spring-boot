package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ChangeOwnerCommand extends AbstractCommand<Item<?, ?>> {

    private final Item<?, ?> item;

    private final User newOwner;

    public ChangeOwnerCommand(FileRepository repository, AccessChecker accessChecker, Item<?, ?> item, User newOwner) {
        super(repository, accessChecker);
        this.item = item;
        this.newOwner = newOwner;
    }

    @Override
    public boolean canExecute(User actor) {
        return accessChecker.canChangeOwner(item, actor);
    }

    @Override
    public Item<?, ?> execute(User actor) {
        Item<?, ?> newItem = item.copyBuilder().withOwner(newOwner).build();
        return repository.save(newItem);
    }

    @Override
    public Error handleError(User actor) {
        return new Error(CommandType.CHOWN, new Input(item, newOwner), actor);
    }
}
