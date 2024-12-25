package io.github.fherbreteau.functional.domain.access.impl;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Item;

public class AllAccessParser implements AccessParser {

    private final AccessContext context;
    private final Item item;

    public AllAccessParser(AccessContext context, Item item) {
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
