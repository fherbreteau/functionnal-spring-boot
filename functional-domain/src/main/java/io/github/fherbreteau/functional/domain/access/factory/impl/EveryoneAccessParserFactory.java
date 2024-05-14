package io.github.fherbreteau.functional.domain.access.factory.impl;

import io.github.fherbreteau.functional.domain.access.AccessRightContext;
import io.github.fherbreteau.functional.domain.access.AccessRightParser;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.access.impl.AllAccessParser;
import io.github.fherbreteau.functional.domain.entities.Item;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static io.github.fherbreteau.functional.domain.access.AccessRightParser.STEP_ATTRIBUTION;

public class EveryoneAccessParserFactory implements AccessParserFactory {
    private static final Predicate<String> ATTRIBUTION_PATTERN = Pattern.compile("a|ugo").asMatchPredicate();

    @Override
    public boolean supports(AccessRightContext context, String rights, Item item) {
        return  Objects.equals(STEP_ATTRIBUTION, context.getStep()) && ATTRIBUTION_PATTERN.test(rights)
                && Objects.nonNull(item);
    }

    @Override
    public AccessRightParser createAccessRightParser(AccessRightContext context, String rights, Item item) {
        return new AllAccessParser(context, item);
    }
}
