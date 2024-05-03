package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ChangeOwnerCommand extends AbstractSuccessCommand {

    private final Item item;

    private final User newOwner;

    public ChangeOwnerCommand(FileRepository repository, Item item, User newOwner) {
        super(repository);
        this.item = item;
        this.newOwner = newOwner;
    }

    @Override
    public Output execute(User actor) {
        Item newItem = item.copyBuilder().withOwner(newOwner).build();
        return new Output(repository.save(newItem));
    }
}
