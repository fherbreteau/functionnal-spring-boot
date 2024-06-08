package io.github.fherbreteau.functional.domain.path.factory;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;

public interface PathFactory {

    boolean supports(Path currentPath, String path);

    PathParser createParser(ItemRepository repository, AccessChecker accessChecker, Path parentPath, String path);

    default int order() {
        return 0;
    }
}
