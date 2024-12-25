package io.github.fherbreteau.functional.domain.access.factory;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.AccessParser;
import io.github.fherbreteau.functional.domain.entities.Item;

public interface CompositeAccessFactory {

    AccessParser createParser(AccessContext context, String rights, Item item);
}
