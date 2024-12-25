package io.github.fherbreteau.functional.domain.access.impl;

import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.ItemInput;

import java.util.function.UnaryOperator;

public class GenericRightAccessParser implements AccessParser {

    private final UnaryOperator<AccessRight> updateFunction;

    public GenericRightAccessParser(UnaryOperator<AccessRight> updateFunction) {
        this.updateFunction = updateFunction;
    }

    @Override
    public AccessRight resolve(ItemInput.Builder builder, AccessRight accessRight) {
        return updateFunction.apply(accessRight);
    }
}
