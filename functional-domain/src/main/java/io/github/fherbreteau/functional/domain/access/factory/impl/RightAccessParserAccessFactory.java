package io.github.fherbreteau.functional.domain.access.factory.impl;

import static io.github.fherbreteau.functional.domain.access.AccessParser.STEP_RIGHT;

import java.util.Objects;
import java.util.regex.Pattern;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.CompositeAccessFactory;
import io.github.fherbreteau.functional.domain.access.factory.RecursiveAccessFactory;
import io.github.fherbreteau.functional.domain.access.impl.RecursiveAccessParser;
import io.github.fherbreteau.functional.domain.entities.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RightAccessParserAccessFactory implements AccessParserFactory, RecursiveAccessFactory {

    private static final Pattern RIGHT_PATTERN = Pattern.compile("[rwx]{2,3}");

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private CompositeAccessFactory compositeAccessFactory;

    @Override
    public boolean supports(AccessContext context, String rights, Item item) {
        return Objects.equals(STEP_RIGHT, context.getStep()) && RIGHT_PATTERN.matcher(rights).matches()
                && Objects.nonNull(item);
    }

    @Override
    public AccessParser createAccessRightParser(AccessContext context, String rights, Item item) {
        logger.debug("Creating access parser");
        return new RecursiveAccessParser(compositeAccessFactory, context, rights, item);
    }

    @Override
    public void setCompositeFactory(CompositeAccessFactory compositeAccessFactory) {
        this.compositeAccessFactory = compositeAccessFactory;
    }
}
