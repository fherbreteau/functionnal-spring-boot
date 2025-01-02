package io.github.fherbreteau.functional.domain.path.impl;

import io.github.fherbreteau.functional.domain.entities.Failure;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.PathParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidPathParser implements PathParser {
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    private final Path currentPath;
    private final String segment;

    public InvalidPathParser(Path currentPath, String segment) {
        this.currentPath = currentPath;
        this.segment = segment;
    }

    @Override
    public Path resolve(User actor) {
        logger.debug("Resolving invalid path {}", segment);
        return currentPath.isError() ? currentPath : Path.error(Failure.failure(String.format("%s not found in %s for %s", segment, currentPath.getHandle(), actor)));
    }
}
