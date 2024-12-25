package io.github.fherbreteau.functional.domain.access.factory.impl;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.CompositeAccessFactory;
import io.github.fherbreteau.functional.domain.access.factory.RecursiveAccessFactory;
import io.github.fherbreteau.functional.domain.access.impl.FullAccessParser;
import io.github.fherbreteau.functional.domain.entities.Item;

import java.util.Objects;
import java.util.function.Predicate;

public class FullAccessParserAccessFactory implements AccessParserFactory, RecursiveAccessFactory {

    private static final Predicate<String> ACCESS_RIGHT_MATCH_PREDICATE =
            FullAccessParser.ACCESS_RIGHT_PATTERN.asMatchPredicate();
    private CompositeAccessFactory compositeAccessFactory;

    @Override
    public boolean supports(AccessContext context, String rights, Item item) {
        return Objects.isNull(context.getStep()) && matches(rights) && Objects.nonNull(item);
    }

    private boolean matches(String rights) {
        return Objects.nonNull(rights) && ACCESS_RIGHT_MATCH_PREDICATE.test(rights);
    }

    @Override
    public AccessParser createAccessRightParser(AccessContext context, String rights, Item item) {
        return new FullAccessParser(compositeAccessFactory, context, rights, item);
    }

    @Override
    public void setCompositeFactory(CompositeAccessFactory compositeAccessFactory) {
        this.compositeAccessFactory = compositeAccessFactory;
    }
}
