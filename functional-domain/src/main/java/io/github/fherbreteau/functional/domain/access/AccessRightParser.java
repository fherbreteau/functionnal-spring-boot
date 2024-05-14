package io.github.fherbreteau.functional.domain.access;

import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Input;

public interface AccessRightParser {

    String STEP_ATTRIBUTION = "attribution";
    String STEP_ACTION = "action";
    String STEP_RIGHT = "right";

    AccessRight resolve(Input.Builder builder, AccessRight accessRight);
}
