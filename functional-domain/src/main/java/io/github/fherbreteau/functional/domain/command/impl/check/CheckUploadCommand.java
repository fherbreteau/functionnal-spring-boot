package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.command.impl.success.UploadCommand;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CheckUploadCommand extends AbstractCheckItemCommand<Item, UploadCommand> {
    private final ContentRepository contentRepository;
    private final File item;
    private final InputStream content;
    private final String contentType;

    public CheckUploadCommand(FileRepository repository, AccessChecker accessChecker,
                              ContentRepository contentRepository, File item, InputStream content, String contentType) {
        super(repository, accessChecker);
        this.contentRepository = contentRepository;
        this.item = item;
        this.content = content;
        this.contentType = contentType;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (!accessChecker.canWrite(item, actor)) {
            reasons.add(String.format("%s can't write %s", actor, item));
        }
        return reasons;
    }

    @Override
    protected UploadCommand createSuccess() {
        return new UploadCommand(repository, contentRepository, item, content, contentType);
    }

    @Override
    protected ItemErrorCommand<Item> createError(List<String> reasons) {
        ItemInput itemInput = ItemInput.builder(item).withContent(content).build();
        return new ItemErrorCommand<>(ItemCommandType.UPLOAD, itemInput, reasons);
    }
}
