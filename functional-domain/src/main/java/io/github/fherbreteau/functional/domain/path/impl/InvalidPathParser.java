package io.github.fherbreteau.functional.domain.path.impl;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.entities.Failure;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.PathParser;

public class InvalidPathParser implements PathParser {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    private final Path currentPath;
    private final String segment;

    public InvalidPathParser(Path currentPath, String segment) {
        this.currentPath = currentPath;
        this.segment = segment;
    }

    @Override
    public Path resolve(User actor) {
        debug(logger, "Resolving invalid path {0}", segment);
        return currentPath.isError() ? currentPath : Path.error(Failure.failure(String.format("%s not found in %s for %s", segment, currentPath.getItem(), actor)));
    }
}
