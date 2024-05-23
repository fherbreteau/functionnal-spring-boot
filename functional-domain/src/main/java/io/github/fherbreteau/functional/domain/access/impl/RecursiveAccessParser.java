package io.github.fherbreteau.functional.domain.access.impl;

import io.github.fherbreteau.functional.domain.access.AccessRightContext;
import io.github.fherbreteau.functional.domain.access.AccessRightParser;
import io.github.fherbreteau.functional.domain.access.CompositeAccessParserFactory;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Item;

public class RecursiveAccessParser implements AccessRightParser {

    private final CompositeAccessParserFactory compositeAccessParserFactory;
    private final AccessRightContext context;
    private final String element;
    private final String rest;
    private final Item item;

    public RecursiveAccessParser(CompositeAccessParserFactory compositeAccessParserFactory, AccessRightContext context, String rights, Item item) {
        this.compositeAccessParserFactory = compositeAccessParserFactory;
        this.context = context;
        element = rights.substring(0, 1);
        rest = rights.substring(1);
        this.item = item;
    }

    @Override
    public AccessRight resolve(ItemInput.Builder builder, AccessRight accessRight) {
        AccessRightParser elementParser = compositeAccessParserFactory.createParser(context, element, item);
        AccessRightParser restParser = compositeAccessParserFactory.createParser(context, rest, item);
        return restParser.resolve(builder, elementParser.resolve(builder, accessRight));
    }
}
