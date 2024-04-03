package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.DownloadCommand;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class DownloadCommandFactory implements CommandFactory<byte[]> {
    @Override
    public boolean supports(CommandType type, Input input) {
        return type == CommandType.DOWNLOAD && input.getItem() instanceof File;
    }

    @Override
    public Command<byte[]> createCommand(FileRepository repository, AccessChecker accessChecker, CommandType type, Input input) {
        return new DownloadCommand(repository, accessChecker, (File) input.getItem());
    }
}
