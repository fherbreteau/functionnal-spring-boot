package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.CommandType;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckChangeModeCommand;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.util.Objects;
import java.util.stream.Stream;

public class ChangeModeCommandFactory implements CommandFactory {

    @Override
    public boolean supports(CommandType commandType, Input input) {
        return commandType == CommandType.CHMOD && isValid(input);
    }

    private boolean isValid(Input input) {
        return input.getItem() != null && Stream.of(input.getAccesses()).anyMatch(Objects::nonNull);
    }

    @Override
    public Command<Command<Output>> createCommand(FileRepository repository, AccessChecker accessChecker, CommandType type, Input input) {
        return new CheckChangeModeCommand(repository, accessChecker, input.getItem(), input.getOwnerAccess(),
                input.getGroupAccess(), input.getOtherAccess());
    }
}
