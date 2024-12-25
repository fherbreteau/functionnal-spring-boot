package io.github.fherbreteau.functional.driven.repository;

import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.Group;

public interface GroupRepository {

    boolean exists(String name);

    Group findByName(String name);

    boolean exists(UUID groupId);

    Group findById(UUID groupId);

    Group create(Group group);

    Group update(Group group);

    void delete(Group group);
}
