package io.github.fherbreteau.functional.domain.access.impl;

import java.util.function.UnaryOperator;

import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericRightAccessParser implements AccessParser {

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private final UnaryOperator<AccessRight> updateFunction;

    public GenericRightAccessParser(UnaryOperator<AccessRight> updateFunction) {
        this.updateFunction = updateFunction;
    }

    @Override
    public AccessRight resolve(ItemInput.Builder builder, AccessRight accessRight) {
        logger.debug("Generic right access parsing");
        return updateFunction.apply(accessRight);
    }
}
