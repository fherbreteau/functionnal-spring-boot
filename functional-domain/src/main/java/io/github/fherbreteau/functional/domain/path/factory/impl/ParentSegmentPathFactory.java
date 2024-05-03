package io.github.fherbreteau.functional.domain.path.factory.impl;

import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.domain.path.factory.PathFactory;
import io.github.fherbreteau.functional.domain.path.impl.NavigationPathParser;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.util.function.Predicate;

public class ParentSegmentPathFactory implements PathFactory {

    private static final String PARENT_PATH = "..";

    public static final Predicate<String> IS_PARENT_PATH = PARENT_PATH::equals;

    @Override
    public boolean supports(Path currentPath, String path) {
        return !currentPath.isError() && currentPath.hasParent() && IS_PARENT_PATH.test(path);
    }

    @Override
    public PathParser createParser(FileRepository repository, AccessChecker accessChecker, Path parentPath, String path) {
        return new NavigationPathParser(parentPath, PARENT_PATH, Item::getParent);
    }
}
