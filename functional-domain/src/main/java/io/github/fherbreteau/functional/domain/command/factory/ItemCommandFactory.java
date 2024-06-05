package io.github.fherbreteau.functional.domain.command.factory;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.ItemRepository;

public interface ItemCommandFactory<T> {

    boolean supports(ItemCommandType type, ItemInput itemInput);

    CheckCommand<T> createCommand(ItemRepository repository, ContentRepository contentRepository,
                                  AccessChecker accessChecker, AccessUpdater accessUpdater,
                                  ItemCommandType type, ItemInput itemInput);

    default int order() {
        return 0;
    }
}
