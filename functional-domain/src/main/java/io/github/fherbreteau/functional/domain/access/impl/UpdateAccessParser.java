package io.github.fherbreteau.functional.domain.access.impl;

import java.util.function.BinaryOperator;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateAccessParser implements AccessParser {

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private final BinaryOperator<AccessRight> accessRightMergeFunction;
    private final AccessContext context;

    public UpdateAccessParser(AccessContext context,
                              BinaryOperator<AccessRight> accessRightMergeFunction) {
        this.context = context;
        this.accessRightMergeFunction = accessRightMergeFunction;
    }

    @Override
    public AccessRight resolve(ItemInput.Builder builder, AccessRight accessRight) {
        logger.debug("Update access parsing");
        this.context.setAccessRightMergeFunction(accessRightMergeFunction);
        return accessRight;
    }
}
