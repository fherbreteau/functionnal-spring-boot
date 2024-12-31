package io.github.fherbreteau.functional.domain.access.factory.impl;

import static io.github.fherbreteau.functional.domain.Logging.debug;
import static io.github.fherbreteau.functional.domain.access.AccessParser.STEP_RIGHT;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.CompositeAccessFactory;
import io.github.fherbreteau.functional.domain.access.factory.RecursiveAccessFactory;
import io.github.fherbreteau.functional.domain.access.impl.RecursiveAccessParser;
import io.github.fherbreteau.functional.domain.entities.Item;

public class RightAccessParserAccessFactory implements AccessParserFactory, RecursiveAccessFactory {

    private static final Pattern RIGHT_PATTERN = Pattern.compile("[rwx]{2,3}");

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());
    private CompositeAccessFactory compositeAccessFactory;

    @Override
    public boolean supports(AccessContext context, String rights, Item item) {
        return Objects.equals(STEP_RIGHT, context.getStep()) && RIGHT_PATTERN.matcher(rights).matches()
                && Objects.nonNull(item);
    }

    @Override
    public AccessParser createAccessRightParser(AccessContext context, String rights, Item item) {
        debug(logger, "Creating access parser");
        return new RecursiveAccessParser(compositeAccessFactory, context, rights, item);
    }

    @Override
    public void setCompositeFactory(CompositeAccessFactory compositeAccessFactory) {
        this.compositeAccessFactory = compositeAccessFactory;
    }
}
