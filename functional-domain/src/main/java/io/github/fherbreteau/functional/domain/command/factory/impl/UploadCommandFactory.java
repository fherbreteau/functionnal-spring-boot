package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckUploadCommand;
import io.github.fherbreteau.functional.domain.entities.CommandType;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

public class UploadCommandFactory implements CommandFactory {
    @Override
    public boolean supports(CommandType type, Input input) {
        return type == CommandType.UPLOAD && input.getItem() instanceof File && input.getContent() != null
                && input.getContentType() != null;
    }

    @Override
    public Command<Command<Output>> createCommand(FileRepository repository, AccessChecker accessChecker,
                                                  ContentRepository contentRepository, CommandType type, Input input) {
        return new CheckUploadCommand(repository, accessChecker, contentRepository, (File) input.getItem(),
                input.getContent(), input.getContentType());
    }
}
