package io.github.fherbreteau.functional.domain.access.factory.impl;

import static io.github.fherbreteau.functional.domain.access.AccessParser.STEP_ATTRIBUTION;

import java.util.Objects;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.access.impl.GenericAttributionAccessParser;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemInput;

public class GroupAccessParserFactory implements AccessParserFactory {
    @Override
    public boolean supports(AccessContext context, String rights, Item item) {
        return Objects.equals(STEP_ATTRIBUTION, context.getStep()) && Objects.equals("g", rights)
                && Objects.nonNull(item);
    }

    @Override
    public AccessParser createAccessRightParser(AccessContext context, String rights, Item item) {
        return new GenericAttributionAccessParser(context, item, ItemInput.Builder::withGroupAccess, Item::getGroupAccess);
    }
}
