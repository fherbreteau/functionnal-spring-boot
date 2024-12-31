package io.github.fherbreteau.functional.domain.access.impl;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.function.BinaryOperator;
import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.ItemInput;

public class UpdateAccessParser implements AccessParser {

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());
    private final BinaryOperator<AccessRight> accessRightMergeFunction;
    private final AccessContext context;

    public UpdateAccessParser(AccessContext context,
                              BinaryOperator<AccessRight> accessRightMergeFunction) {
        this.context = context;
        this.accessRightMergeFunction = accessRightMergeFunction;
    }

    @Override
    public AccessRight resolve(ItemInput.Builder builder, AccessRight accessRight) {
        debug(logger, "Update access parsing");
        this.context.setAccessRightMergeFunction(accessRightMergeFunction);
        return accessRight;
    }
}
