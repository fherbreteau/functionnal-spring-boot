package io.github.fherbreteau.functional.domain.access.factory.impl;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.entities.Item;

public class UnsupportedAccessParserFactory implements AccessParserFactory {
    @Override
    public boolean supports(AccessContext context, String rights, Item item) {
        return true;
    }

    @Override
    public AccessParser createAccessRightParser(AccessContext context, String rights, Item item) {
        return (builder, accessRight) -> null;
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
