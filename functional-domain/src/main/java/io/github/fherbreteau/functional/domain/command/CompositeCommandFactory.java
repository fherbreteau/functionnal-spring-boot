package io.github.fherbreteau.functional.domain.command;

import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.entities.CommandType;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.util.List;

public class CompositeCommandFactory {

    private final FileRepository repository;

    private final AccessChecker accessChecker;
    private final List<CommandFactory> factories;

    public CompositeCommandFactory(FileRepository repository, AccessChecker accessChecker, List<CommandFactory> factories) {
        this.repository = repository;
        this.accessChecker = accessChecker;
        this.factories = factories;
    }

    public Command<Command<Output>> createCommand(CommandType type, Input input) {
        return factories.stream()
                .filter(f -> f.supports(type, input))
                .map(f -> f.createCommand(repository, accessChecker, type, input))
                .findFirst()
                .orElseThrow();
    }
}
