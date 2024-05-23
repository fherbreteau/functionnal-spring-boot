package io.github.fherbreteau.functional.domain.access.impl;

import io.github.fherbreteau.functional.domain.access.AccessRightContext;
import io.github.fherbreteau.functional.domain.access.AccessRightParser;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.ItemInput;

import java.util.function.BinaryOperator;

public class UpdateAccessParser  implements AccessRightParser {

    private final BinaryOperator<AccessRight> accessRightMergeFunction;
    private final AccessRightContext context;

    public UpdateAccessParser(AccessRightContext context,
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
