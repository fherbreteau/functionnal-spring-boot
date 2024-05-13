package io.github.fherbreteau.functional.domain.access.factory.impl;

import io.github.fherbreteau.functional.domain.access.AccessRightContext;
import io.github.fherbreteau.functional.domain.access.AccessRightParser;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.access.impl.GenericAttributionAccessParser;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Item;

import java.util.Objects;

import static io.github.fherbreteau.functional.domain.access.AccessRightParser.STEP_ATTRIBUTION;

public class OtherAccessParserFactory implements AccessParserFactory {
    @Override
    public boolean supports(AccessRightContext context, String rights, Item item) {
        return Objects.equals(STEP_ATTRIBUTION, context.getStep()) && Objects.equals("o", rights)
                && Objects.nonNull(item);
    }

    @Override
    public AccessRightParser createAccessRightParser(AccessRightContext context, String rights, Item item) {
        return new GenericAttributionAccessParser(context, item, Input.Builder::withOtherAccess, Item::getOtherAccess);
    }
}
