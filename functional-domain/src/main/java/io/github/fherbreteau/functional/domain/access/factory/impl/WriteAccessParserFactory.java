package io.github.fherbreteau.functional.domain.access.factory.impl;

import io.github.fherbreteau.functional.domain.access.AccessRightContext;
import io.github.fherbreteau.functional.domain.access.AccessRightParser;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.access.impl.GenericRightAccessParser;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;

import java.util.Objects;

import static io.github.fherbreteau.functional.domain.access.AccessRightParser.STEP_RIGHT;

public class WriteAccessParserFactory implements AccessParserFactory {
    @Override
    public boolean supports(AccessRightContext context, String rights, Item item) {
        return Objects.equals(STEP_RIGHT, context.getStep()) && Objects.equals("w", rights) && Objects.nonNull(item);
    }

    @Override
    public AccessRightParser createAccessRightParser(AccessRightContext context, String rights, Item item) {
        return new GenericRightAccessParser(AccessRight::addWrite);
    }
}
