package io.github.fherbreteau.functional.domain.access.impl;

import io.github.fherbreteau.functional.domain.access.AccessRightContext;
import io.github.fherbreteau.functional.domain.access.AccessRightParser;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Item;

public class AllAccessParser implements AccessRightParser {

    private final AccessRightContext context;
    private final Item item;

    public AllAccessParser(AccessRightContext context, Item item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public AccessRight resolve(ItemInput.Builder builder, AccessRight accessRight) {
        builder.withOwnerAccess(context.applyMergeFunction(accessRight, item.getOwnerAccess()))
                .withGroupAccess(context.applyMergeFunction(accessRight, item.getGroupAccess()))
                .withOtherAccess(context.applyMergeFunction(accessRight, item.getOtherAccess()));
        return accessRight;
    }
}
