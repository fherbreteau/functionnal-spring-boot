package io.github.fherbreteau.functional.domain.path.factory.impl;

import static java.lang.System.Logger.Level.DEBUG;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.domain.path.factory.PathParserFactory;
import io.github.fherbreteau.functional.domain.path.impl.InvalidPathParser;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;

public class InvalidPathParserFactory implements PathParserFactory {
    private final System.Logger logger = System.getLogger(getClass().getSimpleName());

    @Override
    public boolean supports(Path currentPath, String path) {
        return true;
    }

    @Override
    public PathParser createParser(ItemRepository repository, AccessChecker accessChecker, Path parentPath, String path) {
        logger.log(DEBUG, "Creating parser");
        return new InvalidPathParser(parentPath, path);
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
