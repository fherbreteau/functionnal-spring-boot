package io.github.fherbreteau.functional.driving;

import io.github.fherbreteau.functional.domain.access.AccessRightContext;
import io.github.fherbreteau.functional.domain.access.AccessRightParser;
import io.github.fherbreteau.functional.domain.access.CompositeAccessParserFactory;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Item;

import java.util.Objects;

public class AccessParserService {

    private final CompositeAccessParserFactory accessParserFactory;

    public AccessParserService(CompositeAccessParserFactory accessParserFactory) {
        this.accessParserFactory = accessParserFactory;
    }

    public Input parseAccessRights(String rights, Item item) {
        AccessRightParser parser = accessParserFactory.createParser(new AccessRightContext(), rights, item);
        Input.Builder builder = Input.builder(item);
        if (Objects.isNull(parser.resolve(builder, AccessRight.none()))) {
            throw new IllegalStateException("Access right is not valid");
        }
        return builder.build();
    }
}
