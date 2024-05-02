package io.github.fherbreteau.functional.domain.path;

import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;

public interface PathParser {

    Path resolve(User actor);
}
