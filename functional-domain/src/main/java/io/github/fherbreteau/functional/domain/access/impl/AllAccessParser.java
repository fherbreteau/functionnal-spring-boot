package io.github.fherbreteau.functional.domain.access.impl;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllAccessParser implements AccessParser {

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private final AccessContext context;
    private final Item item;

    public AllAccessParser(AccessContext context, Item item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public AccessRight resolve(ItemInput.Builder builder, AccessRight accessRight) {
        logger.debug("Apply access right {} to all", accessRight);
        builder.withOwnerAccess(context.applyMergeFunction(accessRight, item.getOwnerAccess()))
                .withGroupAccess(context.applyMergeFunction(accessRight, item.getGroupAccess()))
                .withOtherAccess(context.applyMergeFunction(accessRight, item.getOtherAccess()));
        return accessRight;
    }
}
