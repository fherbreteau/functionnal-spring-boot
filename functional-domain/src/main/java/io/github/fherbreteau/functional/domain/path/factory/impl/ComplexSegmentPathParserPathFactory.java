package io.github.fherbreteau.functional.domain.path.factory.impl;

import java.util.function.Predicate;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.domain.path.factory.CompositePathFactory;
import io.github.fherbreteau.functional.domain.path.factory.PathParserFactory;
import io.github.fherbreteau.functional.domain.path.factory.RecursivePathFactory;
import io.github.fherbreteau.functional.domain.path.impl.ComplexPathParser;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComplexSegmentPathParserPathFactory implements PathParserFactory, RecursivePathFactory {
    public static final Predicate<String> IS_COMPOSITE_PATH = path -> path.contains("/");
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private CompositePathFactory compositeParserFactory;

    public void setCompositePathFactory(CompositePathFactory compositeParserFactory) {
        this.compositeParserFactory = compositeParserFactory;
    }

    @Override
    public boolean supports(Path currentPath, String path) {
        return !currentPath.isError() && IS_COMPOSITE_PATH.test(path);
    }

    @Override
    public PathParser createParser(ItemRepository repository, AccessChecker accessChecker, Path parentPath, String path) {
        logger.debug("Creating parser");
        return new ComplexPathParser(compositeParserFactory, parentPath, path);
    }
}
