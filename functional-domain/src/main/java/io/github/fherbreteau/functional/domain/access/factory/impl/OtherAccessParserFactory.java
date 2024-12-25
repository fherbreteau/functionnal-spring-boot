package io.github.fherbreteau.functional.domain.access.factory.impl;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.access.impl.GenericAttributionAccessParser;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Item;

import java.util.Objects;

import static io.github.fherbreteau.functional.domain.access.AccessParser.STEP_ATTRIBUTION;

public class OtherAccessParserFactory implements AccessParserFactory {
    @Override
    public boolean supports(AccessContext context, String rights, Item item) {
        return Objects.equals(STEP_ATTRIBUTION, context.getStep()) && Objects.equals("o", rights)
                && Objects.nonNull(item);
    }

    @Override
    public AccessParser createAccessRightParser(AccessContext context, String rights, Item item) {
        return new GenericAttributionAccessParser(context, item, ItemInput.Builder::withOtherAccess, Item::getOtherAccess);
    }
}
