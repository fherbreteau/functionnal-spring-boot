package io.github.fherbreteau.functional.domain.command.factory;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public interface CommandFactory<T> {

    boolean supports(CommandType type, Input input);

    Command<T> createCommand(FileRepository repository, AccessChecker accessChecker, CommandType type, Input input);
}
