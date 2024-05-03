package io.github.fherbreteau.functional.domain.path.impl;

import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.PathParser;

public class InvalidPathParser implements PathParser {

    private final Path currentPath;
    private final String segment;

    public InvalidPathParser(Path currentPath, String segment) {
        this.currentPath = currentPath;
        this.segment = segment;
    }

    @Override
    public Path resolve(User actor) {
        return currentPath.isError() ? currentPath : Path.error(new Error(currentPath.getItem(), segment, actor));
    }
}
