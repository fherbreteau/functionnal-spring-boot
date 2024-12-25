package io.github.fherbreteau.functional.domain.command.impl.check;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.DownloadCommand;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;

public class CheckDownloadCommand extends AbstractCheckItemCommand<InputStream, DownloadCommand> {
    private final ContentRepository contentRepository;
    private final File item;

    public CheckDownloadCommand(ItemRepository repository, AccessChecker accessChecker,
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
    protected ItemErrorCommand<InputStream> createError(List<String> reasons) {
        ItemInput itemInput = ItemInput.builder(item).build();
        return new ItemErrorCommand<>(ItemCommandType.DOWNLOAD, itemInput, reasons);
    }
}
