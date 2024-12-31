package io.github.fherbreteau.functional.domain.access.impl;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.access.factory.CompositeAccessFactory;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemInput;

public class RecursiveAccessParser implements AccessParser {

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());
    private final CompositeAccessFactory compositeAccessFactory;
    private final AccessContext context;
    private final String element;
    private final String rest;
    private final Item item;

    public RecursiveAccessParser(CompositeAccessFactory compositeAccessFactory, AccessContext context, String rights, Item item) {
        this.compositeAccessFactory = compositeAccessFactory;
        this.context = context;
        element = rights.substring(0, 1);
        rest = rights.substring(1);
        this.item = item;
    }

    @Override
    public AccessRight resolve(ItemInput.Builder builder, AccessRight accessRight) {
        debug(logger, "Recursive access parsing");
        AccessParser elementParser = compositeAccessFactory.createParser(context, element, item);
        AccessParser restParser = compositeAccessFactory.createParser(context, rest, item);
        return restParser.resolve(builder, elementParser.resolve(builder, accessRight));
    }
}
