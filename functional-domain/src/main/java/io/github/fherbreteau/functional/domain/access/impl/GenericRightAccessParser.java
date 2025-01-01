package io.github.fherbreteau.functional.domain.access.impl;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.function.UnaryOperator;
import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.ItemInput;

public class GenericRightAccessParser implements AccessParser {

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());
    private final UnaryOperator<AccessRight> updateFunction;

    public GenericRightAccessParser(UnaryOperator<AccessRight> updateFunction) {
        this.updateFunction = updateFunction;
    }

    @Override
    public AccessRight resolve(ItemInput.Builder builder, AccessRight accessRight) {
        debug(logger, "Generic right access parsing");
        return updateFunction.apply(accessRight);
    }
}
