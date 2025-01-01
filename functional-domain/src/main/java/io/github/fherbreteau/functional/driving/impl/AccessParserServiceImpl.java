package io.github.fherbreteau.functional.driving.impl;

import java.util.Objects;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.access.CompositeAccessParserFactory;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.driving.AccessParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessParserServiceImpl implements AccessParserService {
    private final Logger logger = LoggerFactory.getLogger(AccessParserService.class.getSimpleName());

    private final CompositeAccessParserFactory accessParserFactory;

    public AccessParserServiceImpl(CompositeAccessParserFactory accessParserFactory) {
        this.accessParserFactory = accessParserFactory;
    }

    @Override
    public ItemInput parseAccessRights(String rights, Item item) {
        logger.debug("Parsing Access rights for item {}", item);
        AccessParser parser = accessParserFactory.createParser(new AccessContext(), rights, item);
        ItemInput.Builder builder = ItemInput.builder(item);
        if (Objects.isNull(parser.resolve(builder, AccessRight.none()))) {
            throw new IllegalStateException("Access right is not valid");
        }
        return builder.build();
    }
}
