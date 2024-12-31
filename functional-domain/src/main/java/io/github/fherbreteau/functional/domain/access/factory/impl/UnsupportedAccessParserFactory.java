package io.github.fherbreteau.functional.domain.access.factory.impl;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.entities.Item;

public class UnsupportedAccessParserFactory implements AccessParserFactory {

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Override
    public boolean supports(AccessContext context, String rights, Item item) {
        return true;
    }

    @Override
    public AccessParser createAccessRightParser(AccessContext context, String rights, Item item) {
        debug(logger, "Creating access parser");
        return (builder, accessRight) -> null;
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
