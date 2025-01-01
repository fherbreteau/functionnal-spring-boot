package io.github.fherbreteau.functional.domain.access.impl;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.access.factory.CompositeAccessFactory;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemInput;

public class FullAccessParser implements AccessParser {

    private static final String ACCESS_RIGHT_REGEX = "(?<attribution>a|[ugo]{0,3})(?<action>[+-=]?)(?<right>[rwx]{1,3})";
    public static final Pattern ACCESS_RIGHT_PATTERN = Pattern.compile(ACCESS_RIGHT_REGEX);

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());
    private final CompositeAccessFactory compositeAccessFactory;
    private final AccessContext context;
    private final String rights;
    private final Item item;

    public FullAccessParser(CompositeAccessFactory compositeAccessFactory, AccessContext context, String rights, Item item) {
        this.compositeAccessFactory = compositeAccessFactory;
        this.context = context;
        this.rights = rights;
        this.item = item;
    }

    @Override
    public AccessRight resolve(ItemInput.Builder builder, AccessRight accessRight) {
        debug(logger, "Parse access ");
        Matcher matcher = ACCESS_RIGHT_PATTERN.matcher(rights);
        if (!matcher.matches()) {
            return null;
        }
        // Extract the valuable elements
        String attribution = matcher.group(STEP_ATTRIBUTION);
        String action = matcher.group(STEP_ACTION);
        String rightElements = matcher.group(STEP_RIGHT);
        // Parse and resolve the new right
        context.setStep(STEP_RIGHT);
        AccessParser rightParser = compositeAccessFactory.createParser(context, rightElements, item);
        AccessRight newAccessRight = rightParser.resolve(builder, accessRight);
        // Parse and resolve the action
        context.setStep(STEP_ACTION);
        AccessParser actionParser = compositeAccessFactory.createParser(context, action, item);
        newAccessRight = actionParser.resolve(builder, newAccessRight);
        // Parse and resolve the attribution
        context.setStep(STEP_ATTRIBUTION);
        AccessParser attributionParser = compositeAccessFactory.createParser(context, attribution, item);
        return attributionParser.resolve(builder, newAccessRight);
    }
}
