package io.github.fherbreteau.functional.domain.path.impl;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.CompositePathFactory;
import io.github.fherbreteau.functional.domain.path.PathParser;

public class ComplexPathParser implements PathParser {

    private final CompositePathFactory pathFactory;
    private final Path parentPath;
    private final String segment;
    private final String rest;

    public ComplexPathParser(CompositePathFactory pathFactory, Path parentPath, String path) {
        this.pathFactory = pathFactory;
        this.parentPath = parentPath;
        int index = path.indexOf('/');
        this.segment = path.substring(0, index);
        this.rest = (index + 1 == path.length()) ? "" : path.substring(index + 1);
    }

    @Override
    public Path resolve(User actor) {
        PathParser parser = pathFactory.createParser(parentPath, segment);
        Path currentPath = parser.resolve(actor);
        parser = pathFactory.createParser(currentPath, rest);
        return parser.resolve(actor);
    }
}
