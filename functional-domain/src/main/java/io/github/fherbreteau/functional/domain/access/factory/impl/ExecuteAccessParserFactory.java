package io.github.fherbreteau.functional.domain.access.factory.impl;

import static io.github.fherbreteau.functional.domain.Logging.debug;
import static io.github.fherbreteau.functional.domain.access.AccessParser.STEP_RIGHT;

import java.util.Objects;
import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.access.impl.GenericRightAccessParser;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;

public class ExecuteAccessParserFactory implements AccessParserFactory {

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Override
    public boolean supports(AccessContext context, String rights, Item item) {
        return Objects.equals(STEP_RIGHT, context.getStep()) && Objects.equals("x", rights) && Objects.nonNull(item);
    }

    @Override
    public AccessParser createAccessRightParser(AccessContext context, String rights, Item item) {
        debug(logger, "Creating access parser");
        return new GenericRightAccessParser(AccessRight::addExecute);
    }
}
