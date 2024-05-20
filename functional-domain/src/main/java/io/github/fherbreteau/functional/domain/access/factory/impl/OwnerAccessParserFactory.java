package io.github.fherbreteau.functional.domain.access.factory.impl;

import io.github.fherbreteau.functional.domain.access.AccessRightContext;
import io.github.fherbreteau.functional.domain.access.AccessRightParser;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.access.impl.GenericAttributionAccessParser;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Item;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static io.github.fherbreteau.functional.domain.access.AccessRightParser.STEP_ATTRIBUTION;

public class OwnerAccessParserFactory implements AccessParserFactory {
    private static final Predicate<String> ATTRIBUTION_PATTERN = Pattern.compile("u?").asMatchPredicate();

    @Override
    public boolean supports(AccessRightContext context, String rights, Item item) {
        return Objects.equals(STEP_ATTRIBUTION, context.getStep()) && ATTRIBUTION_PATTERN.test(rights)
                && Objects.nonNull(item);
    }

    @Override
    public AccessRightParser createAccessRightParser(AccessRightContext context, String rights, Item item) {
        return new GenericAttributionAccessParser(context, item, ItemInput.Builder::withOwnerAccess, Item::getOwnerAccess);
    }
}
