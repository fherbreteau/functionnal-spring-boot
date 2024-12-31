package io.github.fherbreteau.functional.domain.path.factory.impl;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.function.Predicate;
import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.entities.Path;

public class EmptySegmentPathParserFactory extends CurrentSegmentPathParserFactory {
    public static final Predicate<String> IS_EMPTY_PATH = String::isEmpty;
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Override
    public boolean supports(Path currentPath, String path) {
        debug(logger, "Creating parser");
        return !currentPath.isError() && IS_EMPTY_PATH.test(path);
    }
}
