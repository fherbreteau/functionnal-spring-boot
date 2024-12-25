package io.github.fherbreteau.functional.domain.access.factory.impl;

import static io.github.fherbreteau.functional.domain.access.AccessParser.STEP_RIGHT;

import java.util.Objects;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.access.impl.GenericRightAccessParser;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;

public class ReadAccessParserFactory implements AccessParserFactory {
    @Override
    public boolean supports(AccessContext context, String rights, Item item) {
        return Objects.equals(STEP_RIGHT, context.getStep()) && Objects.equals("r", rights) && Objects.nonNull(item);
    }

    @Override
    public AccessParser createAccessRightParser(AccessContext context, String rights, Item item) {
        return new GenericRightAccessParser(AccessRight::addRead);
    }
}
