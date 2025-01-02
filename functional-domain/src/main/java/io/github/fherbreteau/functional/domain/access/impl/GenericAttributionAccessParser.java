package io.github.fherbreteau.functional.domain.access.impl;

import java.util.function.BiConsumer;
import java.util.function.Function;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericAttributionAccessParser implements AccessParser {

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private final AccessContext context;
    private final Item item;
    private final BiConsumer<ItemInput.Builder, AccessRight> attributionFunction;
    private final Function<Item, AccessRight> itemAccessRightExtractor;

    public GenericAttributionAccessParser(AccessContext context,
                                          Item item,
                                          BiConsumer<ItemInput.Builder, AccessRight> attributionFunction,
                                          Function<Item, AccessRight> itemAccessRightExtractor) {
        this.context = context;
        this.item = item;
        this.attributionFunction = attributionFunction;
        this.itemAccessRightExtractor = itemAccessRightExtractor;
    }

    @Override
    public AccessRight resolve(ItemInput.Builder builder, AccessRight accessRight) {
        logger.debug("Generic attribution access parsing");
        AccessRight itemAccess = itemAccessRightExtractor.apply(item);
        AccessRight newAccess = context.applyMergeFunction(accessRight, itemAccess);
        attributionFunction.accept(builder, newAccess);
        return accessRight;
    }
}
