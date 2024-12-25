package io.github.fherbreteau.functional.domain.access.impl;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.ItemInput;

import java.util.function.BinaryOperator;

public class UpdateAccessParser  implements AccessParser {

    private final BinaryOperator<AccessRight> accessRightMergeFunction;
    private final AccessContext context;

    public UpdateAccessParser(AccessContext context,
                              BinaryOperator<AccessRight> accessRightMergeFunction) {
        this.context = context;
        this.accessRightMergeFunction = accessRightMergeFunction;
    }

    @Override
    public AccessRight resolve(ItemInput.Builder builder, AccessRight accessRight) {
        this.context.setAccessRightMergeFunction(accessRightMergeFunction);
        return accessRight;
    }
}
