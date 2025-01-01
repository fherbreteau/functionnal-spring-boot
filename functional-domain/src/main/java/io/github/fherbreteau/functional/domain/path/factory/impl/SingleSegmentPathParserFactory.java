package io.github.fherbreteau.functional.domain.path.factory.impl;

import static io.github.fherbreteau.functional.domain.path.factory.impl.ComplexSegmentPathParserPathFactory.IS_COMPOSITE_PATH;
import static io.github.fherbreteau.functional.domain.path.factory.impl.CurrentSegmentPathParserFactory.IS_CURRENT_PATH;
import static io.github.fherbreteau.functional.domain.path.factory.impl.EmptySegmentPathParserFactory.IS_EMPTY_PATH;
import static io.github.fherbreteau.functional.domain.path.factory.impl.ParentSegmentPathParserFactory.IS_PARENT_PATH;

import java.util.List;
import java.util.function.Predicate;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.domain.path.factory.PathParserFactory;
import io.github.fherbreteau.functional.domain.path.impl.SimplePathParser;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleSegmentPathParserFactory implements PathParserFactory {
    private static final List<Predicate<String>> PATH_CHECKS = List.of(IS_CURRENT_PATH, IS_PARENT_PATH, IS_EMPTY_PATH, IS_COMPOSITE_PATH);
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    @Override
    public boolean supports(Path currentPath, String path) {
        return !currentPath.isError() && currentPath.isItemFolder() && PATH_CHECKS.stream().noneMatch(p -> p.test(path));
    }

    @Override
    public PathParser createParser(ItemRepository repository, AccessChecker accessChecker, Path parentPath, String path) {
        logger.debug("Creating parser");
        return new SimplePathParser(repository, accessChecker, parentPath, path);
    }
}
