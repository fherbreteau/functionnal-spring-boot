package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.command.impl.ChangeModeCommand;
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
    public Command<?> createCommand(FileRepository repository, AccessChecker accessChecker, CommandType type, Input input) {
        return new ChangeModeCommand(repository, accessChecker, input.getItem(), input.getOwnerAccess(), input.getGroupAccess(), input.getOtherAccess());
    }
}
