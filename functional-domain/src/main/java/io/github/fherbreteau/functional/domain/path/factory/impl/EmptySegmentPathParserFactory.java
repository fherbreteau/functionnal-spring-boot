package io.github.fherbreteau.functional.domain.path.factory.impl;

import static java.lang.System.Logger.Level.DEBUG;

import java.util.function.Predicate;

import io.github.fherbreteau.functional.domain.entities.Path;

public class EmptySegmentPathParserFactory extends CurrentSegmentPathParserFactory {
    private final System.Logger logger = System.getLogger(getClass().getSimpleName());

    public static final Predicate<String> IS_EMPTY_PATH = String::isEmpty;

    @Override
    public boolean supports(Path currentPath, String path) {
        logger.log(DEBUG, "Creating parser");
        return !currentPath.isError() && IS_EMPTY_PATH.test(path);
    }
}
