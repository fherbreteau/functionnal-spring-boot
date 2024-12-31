package io.github.fherbreteau.functional.domain.command.impl.error;

import static java.lang.System.Logger.Level.DEBUG;

import java.util.List;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;

public class ItemErrorCommand<T> implements Command<Output<T>> {

    private final System.Logger logger = System.getLogger(getClass().getSimpleName());

    private final ItemCommandType type;
    private final ItemInput itemInput;
    private final List<String> reasons;

    public ItemErrorCommand(ItemCommandType type, ItemInput itemInput) {
        this(type, itemInput, List.of());
    }

    public ItemErrorCommand(ItemCommandType type, ItemInput itemInput, List<String> reasons) {
        this.type = type;
        this.itemInput = itemInput;
        this.reasons = reasons;
    }

    @Override
    public Output<T> execute(User actor) {
        logger.log(DEBUG, "Command {0} with arguments {1} failed for {2}", type, itemInput, actor);
        return Output.failure(String.format("%s with arguments %s failed for %s", type, itemInput, actor), reasons);
    }
}
