package io.github.fherbreteau.functional.domain.access;

import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.access.factory.RecursiveFactory;

import java.util.Comparator;
import java.util.List;

public class CompositeAccessParserFactory {

    private final List<AccessParserFactory> accessRightParserFactories;

    public CompositeAccessParserFactory(List<AccessParserFactory> accessRightParserFactories) {
        this.accessRightParserFactories = accessRightParserFactories.stream()
                .sorted(Comparator.comparing(AccessParserFactory::order))
                .toList();
    }

    public void configureRecursive() {
        accessRightParserFactories.stream().filter(RecursiveFactory.class::isInstance)
                .map(RecursiveFactory.class::cast)
                .forEach(f -> f.setCompositeAccessParserFactory(this));
    }

    public AccessRightParser createParser(AccessRightContext context, String rights, Item item) {
        return accessRightParserFactories.stream()
                .filter(f -> f.supports(context, rights, item))
                .map(f -> f.createAccessRightParser(context, rights, item))
                .findFirst()
                .orElseThrow();
    }
}
