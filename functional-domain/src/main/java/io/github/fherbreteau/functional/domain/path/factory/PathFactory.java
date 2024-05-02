package io.github.fherbreteau.functional.domain.path.factory;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public interface PathFactory {

    boolean supports(Path currentPath, String path);

    PathParser createParser(FileRepository repository, AccessChecker accessChecker, Path parentPath, String path);
}
