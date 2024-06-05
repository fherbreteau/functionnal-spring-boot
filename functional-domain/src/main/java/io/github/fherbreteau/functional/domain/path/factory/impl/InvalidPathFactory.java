package io.github.fherbreteau.functional.domain.path.factory.impl;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.domain.path.factory.PathFactory;
import io.github.fherbreteau.functional.domain.path.impl.InvalidPathParser;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ItemRepository;

public class InvalidPathFactory implements PathFactory {
    @Override
    public boolean supports(Path currentPath, String path) {
        return true;
    }

    @Override
    public PathParser createParser(ItemRepository repository, AccessChecker accessChecker, Path parentPath, String path) {
        return new InvalidPathParser(parentPath, path);
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
