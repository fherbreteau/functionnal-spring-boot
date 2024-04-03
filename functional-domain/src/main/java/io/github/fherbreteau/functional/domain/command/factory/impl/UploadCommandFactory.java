package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.UploadCommand;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class UploadCommandFactory implements CommandFactory {
    @Override
    public boolean supports(CommandType type, Input input) {
        return type == CommandType.UPLOAD && input.getItem() instanceof File && input.getContent() != null;
    }

    @Override
    public Command<?> createCommand(FileRepository repository, AccessChecker accessChecker, CommandType type, Input input) {
        return new UploadCommand(repository, accessChecker, (File) input.getItem(), input.getContent());
    }
}
