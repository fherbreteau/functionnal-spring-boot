package io.github.fherbreteau.functional.domain.command.impl.check;

import static java.lang.System.Logger.Level.DEBUG;

import java.util.List;

import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;

public class CheckUnsupportedItemCommand extends AbstractCheckItemCommand<Void, ItemErrorCommand<Void>> {

    private final ItemCommandType itemCommandType;

    private final ItemInput itemInput;

    public CheckUnsupportedItemCommand(ItemRepository repository, AccessChecker accessChecker,
                                       ItemCommandType itemCommandType, ItemInput itemInput) {
        super(repository, accessChecker);
        this.itemCommandType = itemCommandType;
        this.itemInput = itemInput;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        return List.of();
    }

    @Override
    protected ItemErrorCommand<Void> createSuccess() {
        logger.log(DEBUG, "Creating error command");
        return new ItemErrorCommand<>(itemCommandType, itemInput);
    }
}
