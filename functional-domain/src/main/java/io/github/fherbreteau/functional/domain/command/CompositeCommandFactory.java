package io.github.fherbreteau.functional.domain.command;

import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.entities.CommandType;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.util.Comparator;
import java.util.List;

public class CompositeCommandFactory {

    private final FileRepository repository;
    private final AccessChecker accessChecker;
    private final ContentRepository contentRepository;
    private final List<CommandFactory> factories;

    public CompositeCommandFactory(FileRepository repository, AccessChecker accessChecker,
                                   ContentRepository contentRepository, List<CommandFactory> factories) {
        this.repository = repository;
        this.accessChecker = accessChecker;
        this.contentRepository = contentRepository;
        this.factories = factories.stream().sorted(Comparator.comparing(CommandFactory::order)).toList();
    }

    public Command<Command<Output>> createCommand(CommandType type, Input input) {
        return factories.stream()
                .filter(f -> f.supports(type, input))
                .map(f -> f.createCommand(repository, accessChecker, contentRepository, type, input))
                .findFirst()
                .orElseThrow();
    }
}
