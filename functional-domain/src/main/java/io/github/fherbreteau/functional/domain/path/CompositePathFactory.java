package io.github.fherbreteau.functional.domain.path;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.factory.PathFactory;
import io.github.fherbreteau.functional.domain.path.factory.RecursiveFactory;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ItemRepository;

import java.util.Comparator;
import java.util.List;

public class CompositePathFactory {

    private final ItemRepository repository;

    private final AccessChecker accessChecker;

    private final List<PathFactory> pathFactories;

    public CompositePathFactory(ItemRepository repository, AccessChecker accessChecker, List<PathFactory> pathFactories) {
        this.repository = repository;
        this.accessChecker = accessChecker;
        this.pathFactories = pathFactories.stream().sorted(Comparator.comparing(PathFactory::order)).toList();
    }

    public void configureRecursive() {
        pathFactories.stream().filter(RecursiveFactory.class::isInstance)
                .map(RecursiveFactory.class::cast)
                .forEach(f -> f.setCompositePathFactory(this));
    }

    public PathParser createParser(Path currentPath, String path) {
        return pathFactories.stream()
                .filter(f -> f.supports(currentPath, path))
                .map(f -> f.createParser(repository, accessChecker, currentPath, path))
                .findFirst()
                .orElseThrow();
    }

}
