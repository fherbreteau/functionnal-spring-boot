package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.entities.CommandType;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.command.impl.success.UploadCommand;
import io.github.fherbreteau.functional.domain.command.impl.error.ErrorCommand;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.io.InputStream;

public class CheckUploadCommand extends AbstractCheckCommand<UploadCommand> {
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
    protected boolean checkAccess(User actor) {
        return accessChecker.canWrite(item, actor);
    }

    @Override
    protected UploadCommand createSuccess() {
        return new UploadCommand(repository, contentRepository, item, content, contentType);
    }

    @Override
    protected ErrorCommand createError() {
        Input input = Input.builder(item).withContent(content).build();
        return new ErrorCommand(CommandType.UPLOAD, input);
    }
}
