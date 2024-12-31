package io.github.fherbreteau.functional.domain.path;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.factory.CompositePathFactory;
import io.github.fherbreteau.functional.domain.path.factory.PathParserFactory;
import io.github.fherbreteau.functional.domain.path.factory.RecursivePathFactory;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;

public class CompositePathParserFactory implements CompositePathFactory {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    private final ItemRepository repository;
    private final AccessChecker accessChecker;
    private final List<PathParserFactory> pathFactories;

    public CompositePathParserFactory(ItemRepository repository, AccessChecker accessChecker, List<PathParserFactory> pathFactories) {
        this.repository = repository;
        this.accessChecker = accessChecker;
        this.pathFactories = pathFactories.stream().sorted(Comparator.comparing(PathParserFactory::order)).toList();
    }

    public void configureRecursive() {
        pathFactories.stream().filter(RecursivePathFactory.class::isInstance)
                .map(RecursivePathFactory.class::cast)
                .forEach(f -> f.setCompositePathFactory(this));
    }

    @Override
    public PathParser createParser(Path currentPath, String path) {
        debug(logger, "Creating parser with {0} on {1}", path, currentPath);
        return pathFactories.stream()
                .filter(f -> f.supports(currentPath, path))
                .map(f -> f.createParser(repository, accessChecker, currentPath, path))
                .findFirst()
                .orElseThrow();
    }

}
