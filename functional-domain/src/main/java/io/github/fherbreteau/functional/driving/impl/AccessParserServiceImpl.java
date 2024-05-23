package io.github.fherbreteau.functional.driving.impl;

import io.github.fherbreteau.functional.domain.access.AccessRightContext;
import io.github.fherbreteau.functional.domain.access.AccessRightParser;
import io.github.fherbreteau.functional.domain.access.CompositeAccessParserFactory;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.driving.AccessParserService;

import java.util.Objects;

public class AccessParserServiceImpl implements AccessParserService {

    private final CompositeAccessParserFactory accessParserFactory;

    public AccessParserServiceImpl(CompositeAccessParserFactory accessParserFactory) {
        this.accessParserFactory = accessParserFactory;
    }

    public ItemInput parseAccessRights(String rights, Item item) {
        AccessRightParser parser = accessParserFactory.createParser(new AccessRightContext(), rights, item);
        ItemInput.Builder builder = ItemInput.builder(item);
        if (Objects.isNull(parser.resolve(builder, AccessRight.none()))) {
            throw new IllegalStateException("Access right is not valid");
        }
        return builder.build();
    }
}
