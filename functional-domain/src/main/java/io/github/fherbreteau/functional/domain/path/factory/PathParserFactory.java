package io.github.fherbreteau.functional.domain.path.factory;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;

public interface PathParserFactory {

    boolean supports(Path currentPath, String path);

    PathParser createParser(ItemRepository repository, AccessChecker accessChecker, Path parentPath, String path);

    default int order() {
        return 0;
    }
}
