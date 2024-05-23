package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ChangeGroupCommand extends AbstractSuccessItemCommand {

    private final Item item;

    private final Group newGroup;

    public ChangeGroupCommand(FileRepository repository, Item item, Group newGroup) {
        super(repository);
        this.item = item;
        this.newGroup = newGroup;
    }

    @Override
    public Output execute(User actor) {
        Item newItem = item.copyBuilder().withGroup(newGroup).build();
        return new Output(repository.save(newItem));
    }
}
