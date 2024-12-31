package io.github.fherbreteau.functional.infra.utils;

import java.util.List;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;

public final class ExtractorUtils {
    private ExtractorUtils() { }

    public static List<UUID> getGroupIds(User actor) {
        return actor.getGroups().stream()
                .map(Group::getGroupId)
                .toList();
    }

}
