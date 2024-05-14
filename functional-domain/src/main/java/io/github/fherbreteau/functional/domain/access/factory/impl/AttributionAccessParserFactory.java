package io.github.fherbreteau.functional.domain.access.factory.impl;

import io.github.fherbreteau.functional.domain.access.AccessRightContext;
import io.github.fherbreteau.functional.domain.access.AccessRightParser;
import io.github.fherbreteau.functional.domain.access.CompositeAccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.RecursiveFactory;
import io.github.fherbreteau.functional.domain.access.impl.RecursiveAccessParser;
import io.github.fherbreteau.functional.domain.entities.Item;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static io.github.fherbreteau.functional.domain.access.AccessRightParser.STEP_ATTRIBUTION;

public class AttributionAccessParserFactory implements AccessParserFactory, RecursiveFactory {

    private static final Predicate<String> ATTRIBUTION_PATTERN = Pattern.compile("[ugo]{2}").asMatchPredicate();
    private CompositeAccessParserFactory compositeAccessParserFactory;

    @Override
    public boolean supports(AccessRightContext context, String rights, Item item) {
        return Objects.equals(STEP_ATTRIBUTION, context.getStep()) && ATTRIBUTION_PATTERN.test(rights)
                && Objects.nonNull(item);
    }

    @Override
    public AccessRightParser createAccessRightParser(AccessRightContext context, String rights, Item item) {
        return new RecursiveAccessParser(compositeAccessParserFactory, context, rights, item);
    }

    @Override
    public void setCompositeAccessParserFactory(CompositeAccessParserFactory compositeAccessParserFactory) {
        this.compositeAccessParserFactory = compositeAccessParserFactory;
    }
}
