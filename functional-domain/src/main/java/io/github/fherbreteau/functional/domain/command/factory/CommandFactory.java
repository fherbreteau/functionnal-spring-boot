package io.github.fherbreteau.functional.domain.command.factory;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.CommandType;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

public interface CommandFactory {

    boolean supports(CommandType type, Input input);

    Command<Command<Output>> createCommand(FileRepository repository, AccessChecker accessChecker,
                                           ContentRepository contentRepository, CommandType type, Input input);

    default int order() {
        return 0;
    }
}
