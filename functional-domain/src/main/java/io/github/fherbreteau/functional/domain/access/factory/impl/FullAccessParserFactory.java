package io.github.fherbreteau.functional.domain.access.factory.impl;

import io.github.fherbreteau.functional.domain.access.AccessRightContext;
import io.github.fherbreteau.functional.domain.access.AccessRightParser;
import io.github.fherbreteau.functional.domain.access.CompositeAccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.RecursiveFactory;
import io.github.fherbreteau.functional.domain.access.impl.FullAccessParser;
import io.github.fherbreteau.functional.domain.entities.Item;

import java.util.Objects;
import java.util.function.Predicate;

public class FullAccessParserFactory implements AccessParserFactory, RecursiveFactory {

    private static final Predicate<String> ACCESS_RIGHT_MATCH_PREDICATE =
            FullAccessParser.ACCESS_RIGHT_PATTERN.asMatchPredicate();
    private CompositeAccessParserFactory compositeAccessParserFactory;

    @Override
    public boolean supports(AccessRightContext context, String rights, Item item) {
        return Objects.isNull(context.getStep()) && matches(rights) && Objects.nonNull(item);
    }

    private boolean matches(String rights) {
        return Objects.nonNull(rights) && ACCESS_RIGHT_MATCH_PREDICATE.test(rights);
    }

    @Override
    public AccessRightParser createAccessRightParser(AccessRightContext context, String rights, Item item) {
        return new FullAccessParser(compositeAccessParserFactory, context, rights, item);
    }

    @Override
    public void setCompositeAccessParserFactory(CompositeAccessParserFactory compositeAccessParserFactory) {
        this.compositeAccessParserFactory = compositeAccessParserFactory;
    }
}
