package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.UnsupportedCommand;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class UnsupportedCommandFactory implements CommandFactory<Void> {
    @Override
    public boolean supports(CommandType type, Input input) {
        return true;
    }

    @Override
    public Command<Void> createCommand(FileRepository repository, AccessChecker accessChecker, CommandType type, Input input) {
        return new UnsupportedCommand(type, input);
    }
}
