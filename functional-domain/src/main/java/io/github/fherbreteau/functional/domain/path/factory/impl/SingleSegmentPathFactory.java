package io.github.fherbreteau.functional.domain.path.factory.impl;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.domain.path.factory.PathFactory;
import io.github.fherbreteau.functional.domain.path.impl.SimplePathParser;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ItemRepository;

import java.util.List;
import java.util.function.Predicate;

import static io.github.fherbreteau.functional.domain.path.factory.impl.ComplexSegmentPathFactory.IS_COMPOSITE_PATH;
import static io.github.fherbreteau.functional.domain.path.factory.impl.CurrentSegmentPathFactory.IS_CURRENT_PATH;
import static io.github.fherbreteau.functional.domain.path.factory.impl.EmptySegmentPathFactory.IS_EMPTY_PATH;
import static io.github.fherbreteau.functional.domain.path.factory.impl.ParentSegmentPathFactory.IS_PARENT_PATH;

public class SingleSegmentPathFactory implements PathFactory {

    private static final List<Predicate<String>> PATH_CHECKS = List.of(IS_CURRENT_PATH, IS_PARENT_PATH, IS_EMPTY_PATH, IS_COMPOSITE_PATH);

    @Override
    public boolean supports(Path currentPath, String path) {
        return !currentPath.isError() && currentPath.isItemFolder() && PATH_CHECKS.stream().noneMatch(p -> p.test(path));
    }

    @Override
    public PathParser createParser(ItemRepository repository, AccessChecker accessChecker, Path parentPath, String path) {
        return new SimplePathParser(repository, accessChecker, parentPath, path);
    }
}
