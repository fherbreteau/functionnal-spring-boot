package io.github.fherbreteau.functional.domain.access.impl;

import io.github.fherbreteau.functional.domain.access.AccessRightContext;
import io.github.fherbreteau.functional.domain.access.AccessRightParser;
import io.github.fherbreteau.functional.domain.access.CompositeAccessParserFactory;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Item;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FullAccessParser implements AccessRightParser {

    private static final String ACCESS_RIGHT_REGEX = "(?<attribution>a|[ugo]{0,3})(?<action>[+-=]?)(?<right>[rwx]{1,3})";
    public static final Pattern ACCESS_RIGHT_PATTERN = Pattern.compile(ACCESS_RIGHT_REGEX);

    private final CompositeAccessParserFactory compositeAccessParserFactory;
    private final AccessRightContext context;
    private final String rights;
    private final Item item;

    public FullAccessParser(CompositeAccessParserFactory compositeAccessParserFactory, AccessRightContext context, String rights, Item item) {
        this.compositeAccessParserFactory = compositeAccessParserFactory;
        this.context = context;
        this.rights = rights;
        this.item = item;
    }

    @Override
    public AccessRight resolve(Input.Builder builder, AccessRight accessRight) {
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
        AccessRightParser rightParser = compositeAccessParserFactory.createParser(context, rightElements, item);
        AccessRight newAccessRight = rightParser.resolve(builder, accessRight);
        // Parse and resolve the action
        context.setStep(STEP_ACTION);
        AccessRightParser actionParser = compositeAccessParserFactory.createParser(context, action, item);
        newAccessRight = actionParser.resolve(builder, newAccessRight);
        // Parse and resolve the attribution
        context.setStep(STEP_ATTRIBUTION);
        AccessRightParser attributionParser = compositeAccessParserFactory.createParser(context, attribution, item);
        return attributionParser.resolve(builder, newAccessRight);
    }
}
