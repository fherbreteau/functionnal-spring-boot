package io.github.fherbreteau.functional.domain.command.impl.error;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.User;

public class ItemErrorCommand implements Command<Output> {

    private final ItemCommandType type;

    private final ItemInput itemInput;

    public ItemErrorCommand(ItemCommandType type, ItemInput itemInput) {
        this.type = type;
        this.itemInput = itemInput;
    }

    @Override
    public Output execute(User actor) {
        return new Output(Error.error(String.format("%s with arguments %s failed for %s", type, itemInput, actor)));
    }
}
