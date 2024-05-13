package io.github.fherbreteau.functional.domain.access.impl;

import io.github.fherbreteau.functional.domain.access.AccessRightContext;
import io.github.fherbreteau.functional.domain.access.AccessRightParser;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Item;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class GenericAttributionAccessParser implements AccessRightParser {

    private final AccessRightContext context;
    private final Item item;
    private final BiConsumer<Input.Builder, AccessRight> attributionFunction;
    private final Function<Item, AccessRight> itemAccessRightExtractor;

    public GenericAttributionAccessParser(AccessRightContext context,
                                          Item item,
                                          BiConsumer<Input.Builder, AccessRight> attributionFunction,
                                          Function<Item, AccessRight> itemAccessRightExtractor) {
        this.context = context;
        this.item = item;
        this.attributionFunction = attributionFunction;
        this.itemAccessRightExtractor = itemAccessRightExtractor;
    }

    @Override
    public AccessRight resolve(Input.Builder builder, AccessRight accessRight) {
        AccessRight itemAccess = itemAccessRightExtractor.apply(item);
        AccessRight newAccess = context.applyMergeFunction(accessRight, itemAccess);
        attributionFunction.accept(builder, newAccess);
        return accessRight;
    }
}
