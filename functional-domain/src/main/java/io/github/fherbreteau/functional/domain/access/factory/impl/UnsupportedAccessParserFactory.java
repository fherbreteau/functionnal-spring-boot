package io.github.fherbreteau.functional.domain.access.factory.impl;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.entities.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnsupportedAccessParserFactory implements AccessParserFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    @Override
    public boolean supports(AccessContext context, String rights, Item item) {
        return true;
    }

    @Override
    public AccessParser createAccessRightParser(AccessContext context, String rights, Item item) {
        logger.debug("Creating access parser");
        return (builder, accessRight) -> null;
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
