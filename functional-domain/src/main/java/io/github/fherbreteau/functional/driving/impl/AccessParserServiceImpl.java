package io.github.fherbreteau.functional.driving.impl;

import static java.lang.System.Logger.Level.DEBUG;

import java.util.Objects;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.access.CompositeAccessParserFactory;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.driving.AccessParserService;

public class AccessParserServiceImpl implements AccessParserService {
    private final System.Logger logger = System.getLogger("AccessParserService");

    private final CompositeAccessParserFactory accessParserFactory;

    public AccessParserServiceImpl(CompositeAccessParserFactory accessParserFactory) {
        this.accessParserFactory = accessParserFactory;
    }

    @Override
    public ItemInput parseAccessRights(String rights, Item item) {
        logger.log(DEBUG, "Parsing Access rights {0} for item {1}", rights, item);
        AccessParser parser = accessParserFactory.createParser(new AccessContext(), rights, item);
        ItemInput.Builder builder = ItemInput.builder(item);
        if (Objects.isNull(parser.resolve(builder, AccessRight.none()))) {
            throw new IllegalStateException("Access right is not valid");
        }
        return builder.build();
    }
}
