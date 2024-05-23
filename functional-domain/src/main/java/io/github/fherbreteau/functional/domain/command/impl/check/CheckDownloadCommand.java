package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.command.impl.success.DownloadCommand;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.util.ArrayList;
import java.util.List;

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
    protected List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (!accessChecker.canRead(item, actor)) {
            reasons.add(String.format("%s can't read %s", actor, item));
        }
        return reasons;
    }

    @Override
    protected DownloadCommand createSuccess() {
        return new DownloadCommand(repository, contentRepository, item);
    }

    @Override
    protected ItemErrorCommand createError(List<String> reasons) {
        ItemInput itemInput = ItemInput.builder(item).build();
        return new ItemErrorCommand(ItemCommandType.DOWNLOAD, itemInput, reasons);
    }
}
