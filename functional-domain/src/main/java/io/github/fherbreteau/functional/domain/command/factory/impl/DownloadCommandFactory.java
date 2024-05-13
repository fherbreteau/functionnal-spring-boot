package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.CommandType;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckDownloadCommand;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

public class DownloadCommandFactory implements CommandFactory {
    @Override
    public boolean supports(CommandType type, Input input) {
        return type == CommandType.DOWNLOAD && input.getItem() instanceof File;
    }

    @Override
    public Command<Command<Output>> createCommand(FileRepository repository, AccessChecker accessChecker,
                                                  ContentRepository contentRepository, CommandType type, Input input) {
        return new CheckDownloadCommand(repository, accessChecker, contentRepository, (File) input.getItem());
    }
}
