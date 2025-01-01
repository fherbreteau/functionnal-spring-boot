package io.github.fherbreteau.functional.domain.command;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public class CompositeItemCommandFactory {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    private final ItemRepository repository;
    private final ContentRepository contentRepository;
    private final AccessChecker accessChecker;
    private final AccessUpdater accessUpdater;
    private final List<ItemCommandFactory<?>> factories;

    public CompositeItemCommandFactory(ItemRepository repository, ContentRepository contentRepository,
                                       AccessChecker accessChecker, AccessUpdater accessUpdater,
                                       List<ItemCommandFactory<?>> factories) {
        this.repository = repository;
        this.accessChecker = accessChecker;
        this.contentRepository = contentRepository;
        this.accessUpdater = accessUpdater;
        this.factories = factories.stream().sorted(Comparator.comparing(ItemCommandFactory::order)).toList();
    }

    @SuppressWarnings("rawtypes")
    public CheckCommand createCommand(ItemCommandType type, ItemInput input) {
        debug(logger, "Looking up for a command of type {0}", type);
        return factories.stream()
                .filter(f -> f.supports(type, input))
                .map(f -> f.createCommand(repository, contentRepository, accessChecker, accessUpdater, type, input))
                .findFirst()
                .orElseThrow();
    }
}
