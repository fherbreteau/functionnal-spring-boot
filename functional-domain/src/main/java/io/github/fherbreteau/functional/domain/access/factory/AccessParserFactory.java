package io.github.fherbreteau.functional.domain.access.factory;

import io.github.fherbreteau.functional.domain.access.AccessRightContext;
import io.github.fherbreteau.functional.domain.access.AccessRightParser;
import io.github.fherbreteau.functional.domain.entities.Item;

public interface AccessParserFactory {

    boolean supports(AccessRightContext context, String rights, Item item);

    AccessRightParser createAccessRightParser(AccessRightContext context, String rights, Item item);

    default int order() {
        return 0;
    }
}
