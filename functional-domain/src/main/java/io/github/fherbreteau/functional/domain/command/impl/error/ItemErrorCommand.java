package io.github.fherbreteau.functional.domain.command.impl.error;

import java.util.List;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemErrorCommand<T> implements Command<Output<T>> {

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

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
        logger.debug("Command {} with arguments {} failed for {}", type, itemInput, actor);
        return Output.failure(String.format("%s with arguments %s failed for %s", type, itemInput, actor), reasons);
    }
}
