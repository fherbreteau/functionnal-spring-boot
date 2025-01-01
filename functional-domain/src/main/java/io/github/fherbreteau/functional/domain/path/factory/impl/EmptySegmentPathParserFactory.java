package io.github.fherbreteau.functional.domain.path.factory.impl;

import java.util.function.Predicate;

import io.github.fherbreteau.functional.domain.entities.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmptySegmentPathParserFactory extends CurrentSegmentPathParserFactory {
    public static final Predicate<String> IS_EMPTY_PATH = String::isEmpty;
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    @Override
    public boolean supports(Path currentPath, String path) {
        logger.debug("Creating parser");
        return !currentPath.isError() && IS_EMPTY_PATH.test(path);
    }
}
