package io.github.fherbreteau.functional.domain.path.factory.impl;

import static java.lang.System.Logger.Level.DEBUG;

import java.util.function.Predicate;

import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.domain.path.factory.PathParserFactory;
import io.github.fherbreteau.functional.domain.path.impl.NavigationPathParser;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;

public class ParentSegmentPathParserFactory implements PathParserFactory {
    private final System.Logger logger = System.getLogger(getClass().getSimpleName());

    private static final String PARENT_PATH = "..";

    public static final Predicate<String> IS_PARENT_PATH = PARENT_PATH::equals;

    @Override
    public boolean supports(Path currentPath, String path) {
        return !currentPath.isError() && currentPath.hasParent() && IS_PARENT_PATH.test(path);
    }

    @Override
    public PathParser createParser(ItemRepository repository, AccessChecker accessChecker, Path parentPath, String path) {
        logger.log(DEBUG, "Creating parser");
        return new NavigationPathParser(parentPath, PARENT_PATH, Item::getParent);
    }
}
