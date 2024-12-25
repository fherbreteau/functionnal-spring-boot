package io.github.fherbreteau.functional.domain.path.factory.impl;

import io.github.fherbreteau.functional.domain.entities.Path;

import java.util.function.Predicate;

public class EmptySegmentPathParserFactory extends CurrentSegmentPathParserFactory {

    public static final Predicate<String> IS_EMPTY_PATH = String::isEmpty;

    @Override
    public boolean supports(Path currentPath, String path) {
        return !currentPath.isError() && IS_EMPTY_PATH.test(path);
    }
}
