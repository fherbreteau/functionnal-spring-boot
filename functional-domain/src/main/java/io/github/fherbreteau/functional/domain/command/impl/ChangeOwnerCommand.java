package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ChangeOwnerCommand<I extends Item<I, ?>> extends AbstractCommand<I> {

    private final I item;

    private final User newOwner;

    public ChangeOwnerCommand(FileRepository repository, AccessChecker accessChecker, I item, User newOwner) {
        super(repository, accessChecker);
        this.item = item;
        this.newOwner = newOwner;
    }

    @Override
    public boolean canExecute(User actor) {
        return accessChecker.canChangeOwner(item, actor);
    }

    @Override
    public I execute(User actor) {
        I newItem = item.copyBuilder().withOwner(newOwner).build();
        return repository.save(newItem);
    }

    @Override
    public Error handleError(User actor) {
        Input input = Input.builder(item)
                .withUser(newOwner)
                .build();
        return new Error(CommandType.CHOWN, input, actor);
    }
}
