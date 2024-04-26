package io.github.fherbreteau.functional.domain.command.factory;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.command.Output;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public interface CommandFactory {

    boolean supports(CommandType type, Input input);

    Command<Command<Output>> createCommand(FileRepository repository, AccessChecker accessChecker, CommandType type, Input input);
}
