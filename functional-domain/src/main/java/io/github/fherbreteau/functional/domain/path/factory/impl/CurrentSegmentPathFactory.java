package io.github.fherbreteau.functional.domain.path.factory.impl;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.domain.path.factory.PathFactory;
import io.github.fherbreteau.functional.domain.path.impl.NavigationPathParser;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class CurrentSegmentPathFactory implements PathFactory {

    private static final String CURRENT_PATH = ".";

    public static final Predicate<String> IS_CURRENT_PATH = CURRENT_PATH::equals;

    @Override
    public boolean supports(Path currentPath, String path) {
        return !currentPath.isError() && IS_CURRENT_PATH.test(path);
    }

    @Override
    public PathParser createParser(FileRepository repository, AccessChecker accessChecker, Path parentPath, String path) {
        return new NavigationPathParser(parentPath, CURRENT_PATH, UnaryOperator.identity());
    }
}
