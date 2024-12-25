package io.github.fherbreteau.functional.domain.path.impl;

import static io.github.fherbreteau.functional.domain.path.factory.impl.CurrentSegmentPathParserFactory.IS_CURRENT_PATH;

import java.util.Optional;
import java.util.function.UnaryOperator;

import io.github.fherbreteau.functional.domain.entities.Failure;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.PathParser;

public class NavigationPathParser implements PathParser {
    private final Path parentPath;
    private final UnaryOperator<Item> itemFunction;
    private final String path;

    public NavigationPathParser(Path parentPath, String path, UnaryOperator<Item> itemFunction) {
        this.parentPath = parentPath;
        this.path = path;
        this.itemFunction = itemFunction;
    }

    @Override
    public Path resolve(User actor) {
        return Optional.of(parentPath)
                .filter(p -> !parentPath.isItemFile() || !IS_CURRENT_PATH.test(path))
                .map(Path::getItem)
                .map(itemFunction)
                .map(Path::success)
                .orElseGet(() -> Path.error(Failure.failure(String.format("%s not found in %s for %s", path, parentPath.getItem(), actor))));
    }
}
