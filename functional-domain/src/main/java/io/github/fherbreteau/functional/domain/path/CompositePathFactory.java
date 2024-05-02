package io.github.fherbreteau.functional.domain.path;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.factory.PathFactory;
import io.github.fherbreteau.functional.domain.path.factory.RecursiveFactory;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.util.List;

public class CompositePathFactory {

    private final FileRepository repository;

    private final AccessChecker accessChecker;

    private final List<PathFactory> pathFactories;

    public CompositePathFactory(FileRepository repository, AccessChecker accessChecker, List<PathFactory> pathFactories) {
        this.repository = repository;
        this.accessChecker = accessChecker;
        this.pathFactories = pathFactories;
    }

    public void configureRecursives() {
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
