package io.github.fherbreteau.functional.infra;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.driven.GroupRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class GroupRepositoryImpl implements GroupRepository {

    private static final String NOT_IMPLEMENTED = "Not Implemented Yet";

    @Override
    public boolean exists(String name) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public Group findByName(String name) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean exists(UUID groupId) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public Group findById(UUID groupId) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public Group save(Group group) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public Group delete(Group group) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }
}
