package io.github.fherbreteau.functional.domain.path.impl;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.domain.path.factory.CompositePathFactory;

public class ComplexPathParser implements PathParser {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

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
        debug(logger, "Resolving path {0} and {1} in parent {2}", segment, rest, segment);
        PathParser parser = pathFactory.createParser(parentPath, segment);
        Path currentPath = parser.resolve(actor);
        parser = pathFactory.createParser(currentPath, rest);
        return parser.resolve(actor);
    }
}
