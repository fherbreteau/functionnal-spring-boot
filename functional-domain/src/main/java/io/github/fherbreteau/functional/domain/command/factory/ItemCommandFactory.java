package io.github.fherbreteau.functional.domain.command.factory;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

public interface ItemCommandFactory {

    boolean supports(ItemCommandType type, ItemInput itemInput);

    CheckCommand<Output> createCommand(FileRepository repository, ContentRepository contentRepository,
                                       AccessChecker accessChecker, AccessUpdater accessUpdater,
                                       ItemCommandType type, ItemInput itemInput);

    default int order() {
        return 0;
    }
}
