package io.github.fherbreteau.functional.domain.path.factory;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.PathParser;

public interface CompositePathFactory {

    PathParser createParser(Path currentPath, String path);
}
