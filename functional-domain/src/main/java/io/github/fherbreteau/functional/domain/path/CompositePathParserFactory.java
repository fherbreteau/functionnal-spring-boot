package io.github.fherbreteau.functional.domain.path;

import static java.lang.System.Logger.Level.DEBUG;

import java.util.Comparator;
import java.util.List;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.factory.CompositePathFactory;
import io.github.fherbreteau.functional.domain.path.factory.PathParserFactory;
import io.github.fherbreteau.functional.domain.path.factory.RecursivePathFactory;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;

public class CompositePathParserFactory implements CompositePathFactory {
    private final System.Logger logger = System.getLogger(getClass().getSimpleName());

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
        logger.log(DEBUG, "Creating parser with {0} on {1}", path, currentPath);
        return pathFactories.stream()
                .filter(f -> f.supports(currentPath, path))
                .map(f -> f.createParser(repository, accessChecker, currentPath, path))
                .findFirst()
                .orElseThrow();
    }

}
