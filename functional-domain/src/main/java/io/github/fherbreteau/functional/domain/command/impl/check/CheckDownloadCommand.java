package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.command.impl.success.DownloadCommand;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CheckDownloadCommand extends AbstractCheckItemCommand<DownloadCommand> {
    private final ContentRepository contentRepository;
    private final File item;

    public CheckDownloadCommand(FileRepository repository, AccessChecker accessChecker,
                                ContentRepository contentRepository, File item) {
        super(repository, accessChecker);
        this.contentRepository = contentRepository;
        this.item = item;
    }

    @Override
    protected boolean checkAccess(User actor) {
        return accessChecker.canRead(item, actor);
    }

    @Override
    protected DownloadCommand createSuccess() {
        return new DownloadCommand(repository, contentRepository, item);
    }

    @Override
    protected ItemErrorCommand createError() {
        ItemInput itemInput = ItemInput.builder(item).build();
        return new ItemErrorCommand(ItemCommandType.DOWNLOAD, itemInput);
    }
}
