package io.github.fherbreteau.functional.domain.path.impl;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.domain.path.factory.CompositePathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComplexPathParser implements PathParser {
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    private final CompositePathFactory pathFactory;
    private final Path parentPath;
    private final String segment;
    private final String rest;

    public ComplexPathParser(CompositePathFactory pathFactory, Path parentPath, String path) {
        this.pathFactory = pathFactory;
        this.parentPath = parentPath;
        int index = path.indexOf('/');
        this.segment = path.substring(0, index);
        // Increment the index to skip the path separator
        index++;
        this.rest = (index == path.length()) ? "" : path.substring(index);
    }

    @Override
    public Path resolve(User actor) {
        logger.debug("Resolving path {} and {} in parent {}", segment, rest, segment);
        PathParser parser = pathFactory.createParser(parentPath, segment);
        Path currentPath = parser.resolve(actor);
        parser = pathFactory.createParser(currentPath, rest);
        return parser.resolve(actor);
    }
}
