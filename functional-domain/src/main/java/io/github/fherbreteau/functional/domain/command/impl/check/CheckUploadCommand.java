package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.command.impl.success.UploadCommand;
import io.github.fherbreteau.functional.domain.command.impl.error.ErrorCommand;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CheckUploadCommand extends AbstractCheckCommand<UploadCommand> {

    private final File item;

    private final byte[] content;

    public CheckUploadCommand(FileRepository repository, AccessChecker accessChecker, File item, byte[] content) {
        super(repository, accessChecker);
        this.item = item;
        this.content = content;
    }

    @Override
    protected boolean checkAccess(User actor) {
        return accessChecker.canWrite(item, actor);
    }

    @Override
    protected UploadCommand createSuccess() {
        return new UploadCommand(repository, item, content);
    }

    @Override
    protected ErrorCommand createError() {
        Input input = Input.builder(item).withContent(content).build();
        return new ErrorCommand(CommandType.UPLOAD, input);
    }
}
