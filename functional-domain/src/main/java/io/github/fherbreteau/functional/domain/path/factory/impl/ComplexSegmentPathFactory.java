package io.github.fherbreteau.functional.domain.path.factory.impl;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.CompositePathFactory;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.domain.path.factory.PathFactory;
import io.github.fherbreteau.functional.domain.path.factory.RecursiveFactory;
import io.github.fherbreteau.functional.domain.path.impl.ComplexPathParser;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ItemRepository;

import java.util.function.Predicate;

public class ComplexSegmentPathFactory implements PathFactory, RecursiveFactory {

    public static final Predicate<String> IS_COMPOSITE_PATH = path -> path.contains("/");

    private CompositePathFactory compositePathFactory;

    public void setCompositePathFactory(CompositePathFactory compositePathFactory) {
        this.compositePathFactory = compositePathFactory;
    }

    @Override
    public boolean supports(Path currentPath, String path) {
        return !currentPath.isError() && IS_COMPOSITE_PATH.test(path);
    }

    @Override
    public PathParser createParser(ItemRepository repository, AccessChecker accessChecker, Path parentPath, String path) {
        return new ComplexPathParser(compositePathFactory, parentPath, path);
    }
}
