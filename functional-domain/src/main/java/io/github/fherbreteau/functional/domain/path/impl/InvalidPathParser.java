package io.github.fherbreteau.functional.domain.path.impl;

import static java.lang.System.Logger.Level.DEBUG;

import io.github.fherbreteau.functional.domain.entities.Failure;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.PathParser;

public class InvalidPathParser implements PathParser {
    private final System.Logger logger = System.getLogger(getClass().getSimpleName());

    private final Path currentPath;
    private final String segment;

    public InvalidPathParser(Path currentPath, String segment) {
        this.currentPath = currentPath;
        this.segment = segment;
    }

    @Override
    public Path resolve(User actor) {
        logger.log(DEBUG, "Resolving invalid path {0}", segment);
        return currentPath.isError() ? currentPath : Path.error(Failure.failure(String.format("%s not found in %s for %s", segment, currentPath.getItem(), actor)));
    }
}
