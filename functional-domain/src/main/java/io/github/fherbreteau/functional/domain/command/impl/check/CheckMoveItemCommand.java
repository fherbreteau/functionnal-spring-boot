package io.github.fherbreteau.functional.domain.command.impl.check;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import io.github.fherbreteau.functional.domain.command.impl.success.MoveItemCommand;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public class CheckMoveItemCommand extends AbstractCheckLocationItemCommand<MoveItemCommand> {

    public CheckMoveItemCommand(ItemRepository repository, AccessChecker accessChecker, AccessUpdater accessUpdater,
                                Item source, Item destination) {
        super(repository, accessChecker, accessUpdater, source, destination);
    }

    @Override
    protected ItemCommandType getCommand() {
        return ItemCommandType.MOVE;
    }

    @Override
    protected MoveItemCommand createSuccess() {
        debug(logger,  "Creating execute command");
        return new MoveItemCommand(repository, accessUpdater, source, destination);
    }
}
